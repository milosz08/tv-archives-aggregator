/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.miloszgilga.tvarchiver.dataserver.features.tvchannel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.config.AppConfig;
import pl.miloszgilga.tvarchiver.dataserver.features.tvchannel.dto.*;
import pl.miloszgilga.tvarchiver.dataserver.util.AppUtils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class TvChannelServiceImpl implements TvChannelService {
	private final JdbcTemplate jdbcTemplate;
	private final AppConfig appConfig;

	@Override
	public Map<Character, List<TvChannelResponseDto>> getTvChannelsBySearch(String phrase, boolean onlyWithSomeData) {
		String sql = """
			SELECT c.name, slug, COUNT(DISTINCT DATE(schedule_date)) AS persisted_days FROM tv_channels AS c
			LEFT JOIN tv_programs_data AS d ON d.channel_id = c.id
			WHERE LOWER(c.name) LIKE LOWER(?) GROUP BY c.id
			""";
		final List<TvChannelResponseDto> tvChannels = jdbcTemplate
			.query(sql, new DataClassRowMapper<>(TvChannelResponseDto.class), "%" + phrase + "%");

		final TreeMap<Character, List<TvChannelResponseDto>> mappedByFirstLetter = new TreeMap<>(tvChannels.stream()
			.filter(channel -> channel.persistedDays() > 0 || !onlyWithSomeData)
			.collect(Collectors.groupingBy(s -> s.name().toLowerCase().charAt(0))));

		final List<TvChannelResponseDto> nonLettersNames = new ArrayList<>();
		final List<Character> elementsToRemoved = new ArrayList<>();

		// filter for non-letter characters
		for (final Map.Entry<Character, List<TvChannelResponseDto>> entry : mappedByFirstLetter.entrySet()) {
			if (!Character.isLetter(entry.getKey())) {
				nonLettersNames.addAll(entry.getValue());
				elementsToRemoved.add(entry.getKey());
			}
		}
		// removing moved letters
		for (final Character letter : elementsToRemoved) {
			mappedByFirstLetter.remove(letter);
		}
		if (!nonLettersNames.isEmpty()) {
			mappedByFirstLetter.put('#', nonLettersNames); // add saved non-letters tv channels
		}
		// sort all channels in selected letter category
		for (final List<TvChannelResponseDto> channels : mappedByFirstLetter.values()) {
			Collections.sort(channels);
		}
		return mappedByFirstLetter;
	}

	@Override
	public TvChannelDetailsDto getTvChannelDetails(String channelSlug) {
		final String sql = """
				SELECT c.name, COUNT(d.id) > 0 AS has_persisted_days
				FROM tv_channels AS c
				LEFT JOIN tv_programs_data AS d ON d.channel_id = c.id
				WHERE slug = ?
				GROUP BY c.id
			""";
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(TvChannelDetailsDto.class), channelSlug);
	}

	@Override
	public TvChannelPersistenceInfoDto getTvChannelPersistenceDetails(String channelSlug) {
		final String sql = """
			SELECT
				COUNT(DISTINCT DATE(schedule_date)) as persisted_days,
				COUNT(DISTINCT YEAR(schedule_date)) as persisted_years,
				COUNT(d.id) as persisted_tv_programs,
				SUM(
			    IF(ISNULL(d.id), 0, 28) +
			    IFNULL(LENGTH(d.name), 0) +
			    IFNULL(LENGTH(d.description), 0) +
			    IFNULL(LENGTH(d.program_type), 0) +
			    IFNULL(LENGTH(d.badge), 0) +
			    IFNULL(LENGTH(d.hour_start), 0) +
			    IFNULL(LENGTH(d.schedule_date), 0) +
			    IFNULL(LENGTH(d.weekday), 0) +
			    IFNULL(LENGTH(d.channel_id), 0)
				) AS average_db_size
			FROM tv_channels AS c LEFT JOIN tv_programs_data AS d ON d.channel_id = c.id
			WHERE slug = ? GROUP BY c.id
			""";
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(TvChannelPersistenceInfoDto.class), channelSlug);
	}

	@Override
	public MonthlyProgramsChartDto getMonthlyChannelPrograms(String channelSlug, int year) {
		// fetch all persisted months for selected year in channel
		final String persistedMonthsSql = """
			SELECT DISTINCT MONTH(schedule_date) FROM tv_programs_data d
			INNER JOIN tv_channels c ON d.channel_id = c.id
			WHERE slug = ? AND YEAR(schedule_date) = ?
			""";
		final List<String> persistedMonths = jdbcTemplate
			.queryForList(persistedMonthsSql, Integer.class, channelSlug, year)
			.stream()
			.map(monthId -> Month.of(monthId).getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
			.toList();

		// get all program types combined with freq for all months separately
		final String programTypesPerYearSql = """
			SELECT program_type AS program_type, COUNT(d.id) AS count_per_month, MONTH(schedule_date) as month_id
			FROM tv_programs_data d
			INNER JOIN tv_channels c ON d.channel_id = c.id
			WHERE slug = ? AND YEAR(schedule_date) = ?
			GROUP BY program_type, MONTH(schedule_date) ORDER BY MONTH(schedule_date) ASC
			""";
		final List<ProgramTypeDto> programTypesPerYear = jdbcTemplate
			.query(programTypesPerYearSql, new DataClassRowMapper<>(ProgramTypeDto.class), channelSlug, year);

		// flatted to program types
		final List<String> programTypes = programTypesPerYear.stream()
			.map(ProgramTypeDto::programType)
			.distinct()
			.toList();

		// create map with key as program type and value as months
		final Map<String, List<MonthlyData>> programTypesMap = programTypesPerYear.stream()
			.collect(Collectors
				.groupingBy(ProgramTypeDto::programType, Collectors.mapping(MonthlyData::new, Collectors.toList())));

		final String stackKey = UUID.randomUUID().toString();
		final List<MonthlyProgramChartStackElement> tableData = new ArrayList<>(persistedMonths.size());

		for (final String programType : programTypes) {
			final List<MonthlyData> monthlyData = programTypesMap.get(programType);
			if (monthlyData == null) {
				continue;
			}
			final Map<Integer, Integer> monthFreqMap = monthlyData.stream()
				.collect(Collectors.toMap(MonthlyData::monthId, MonthlyData::freq));

			List<MonthlyData> allMonthsData = new ArrayList<>();
			for (int month = 1; month <= persistedMonths.size(); month++) {
				allMonthsData.add(new MonthlyData(month, monthFreqMap.getOrDefault(month, 0)));
			}
			// sort programs freq months from 1 -> 12
			allMonthsData.sort(Comparator.comparing(MonthlyData::monthId));

			final List<Integer> monthsFreq = allMonthsData.stream()
				.map(MonthlyData::freq)
				.toList();

			tableData.add(new MonthlyProgramChartStackElement(programType, monthsFreq));
		}
		tableData.sort(Comparator.comparing(row -> row.getData().stream().mapToInt(Integer::intValue).sum(),
			Comparator.reverseOrder()));

		final List<MonthlyProgramChartStackElement> series = new ArrayList<>(persistedMonths.size());

		final List<Integer> allChannelRecordsInMonth = AppUtils.reduce2dList(
			persistedMonths.size(),
			tableData.stream()
				.map(MonthlyProgramChartStackElement::getData)
				.toList()
		);
		final int colorsCount = appConfig.getChartColors().size();
		for (int i = 0; i < tableData.size(); i++) {
			final String color = i > colorsCount - 1 ? appConfig.getDefaultChartColor() : appConfig.getChartColors().get(i);

			final MonthlyProgramChartStackElement tableRow = tableData.get(i);
			tableRow.setColor(color);
			if (i < colorsCount) {
				series.add(tableRow);
				tableRow.setExistInChart(true);
			} else if (i == colorsCount) {
				final List<List<Integer>> restOfData = tableData.subList(colorsCount, tableData.size())
					.stream()
					.map(MonthlyProgramChartStackElement::getData)
					.toList();
				series.add(new MonthlyProgramChartStackElement("Others",
					AppUtils.reduce2dList(persistedMonths.size(), restOfData), appConfig.getDefaultChartColor(), true));
			}
		}
		series.sort(Comparator.comparing(row -> row.getData().stream().mapToInt(Integer::intValue).sum(),
			Comparator.reverseOrder()));

		return new MonthlyProgramsChartDto(series, tableData, persistedMonths, stackKey, allChannelRecordsInMonth);
	}
}
