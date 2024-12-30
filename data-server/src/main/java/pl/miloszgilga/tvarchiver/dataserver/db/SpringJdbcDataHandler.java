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

package pl.miloszgilga.tvarchiver.dataserver.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.config.AppConfig;
import pl.miloszgilga.tvarchiver.dataserver.features.calendar.dto.MinMaxDateDto;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.*;
import pl.miloszgilga.tvarchiver.dataserver.features.tvchannel.dto.ProgramTypeDto;
import pl.miloszgilga.tvarchiver.dataserver.features.tvchannel.dto.TvChannelDetailsDto;
import pl.miloszgilga.tvarchiver.dataserver.features.util.dto.DatabaseCapacityDetailsDto;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SpringJdbcDataHandler implements DataHandler {
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final AppConfig appConfig;

	@Override
	public MinMaxDateDto getMinAndMaxDate(String channelSlug, int year) {
		final String sql = String.format("""
			SELECT MIN(schedule_date) AS start, MAX(schedule_date) AS end
			FROM `%s`
			WHERE YEAR(schedule_date) = ?
			""", channelToTableName(channelSlug));
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(MinMaxDateDto.class), year);
	}

	@Override
	public List<String> getChannelPersistedYears(String channelSlug) {
		final String sql = String.format("""
			SELECT DISTINCT YEAR(schedule_date)
			FROM `%s`
			ORDER BY YEAR(schedule_date) DESC
			""", channelToTableName(channelSlug));
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public String getChannelName(String channelSlug) {
		final String sql = "SELECT name FROM tv_channels WHERE slug = ?";
		return jdbcTemplate.queryForObject(sql, String.class, channelSlug);
	}

	@Override
	public List<ProgramDto> getChannelProgramForDay(String channelSlug, LocalDate day) {
		final String sql = String.format("""
			SELECT name, description, program_type,
			IF(season IS NULL OR episode IS NULL, false, true) AS isTvShow,
			season, episode, badge, hour_start
			FROM `%s`
			WHERE schedule_date = ?
			""", channelToTableName(channelSlug));
		return jdbcTemplate.query(sql, new DataClassRowMapper<>(ProgramDto.class), day);
	}

	@Override
	public TvChannelDetailsDto getChannelDetails(String channelSlug) {
		final String sql = String.format("""
			SELECT name, (SELECT COUNT(*)
				FROM information_schema.tables
			  WHERE table_schema = '%s'
			  AND table_name = '%s') > 0 AS has_persisted_days
			FROM tv_channels WHERE slug = ?
			""", appConfig.getDatabaseName(), channelToTableName(channelSlug));
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(TvChannelDetailsDto.class), channelSlug);
	}

	@Override
	public List<Integer> getPersistedMonths(String channelSlug, int year) {
		final String sql = String.format("""
			SELECT DISTINCT MONTH(schedule_date) FROM `%s`
			WHERE YEAR(schedule_date) = ?
			""", channelToTableName(channelSlug));
		return jdbcTemplate.queryForList(sql, Integer.class, year);
	}

	@Override
	public List<ProgramTypeDto> getProgramTypesPerYear(String channelSlug, int year) {
		final String sql = String.format("""
			SELECT program_type, COUNT(id) AS count_per_month, MONTH(schedule_date) as month_id
			FROM `%s`
			WHERE YEAR(schedule_date) = ?
			GROUP BY program_type, MONTH(schedule_date) ORDER BY MONTH(schedule_date) ASC
			""", channelToTableName(channelSlug));
		return jdbcTemplate.query(sql, new DataClassRowMapper<>(ProgramTypeDto.class), year);
	}

	@Override
	public List<SelectRecordDto> getChannelsWithAnyContent() {
		final String sql = String.format("""
			SELECT slug AS id, name AS value FROM tv_channels AS c
			WHERE (SELECT COUNT(*)
				FROM information_schema.tables
			  WHERE table_schema = '%s'
			  AND table_name = CONCAT('channel_', REPLACE(c.slug, '-', '_'))) > 0
			""", appConfig.getDatabaseName());
		return jdbcTemplate.query(sql, new DataClassRowMapper<>(SelectRecordDto.class));
	}

	@Override
	public List<String> getPersistedChannels() {
		final String sql = """
			SELECT REPLACE(REPLACE(table_name, '_', '-'), 'channel-', '')
			FROM information_schema.tables
			WHERE table_schema = ? AND table_name LIKE 'channel_%'
			""";
		return jdbcTemplate.queryForList(sql, String.class, appConfig.getDatabaseName());
	}

	@Override
	public Map<String, Long> getProgramTypes(List<String> channelSlugs) {
		final String sql = unionWrapper(channelSlugs, slug -> String.format("""
			SELECT program_type AS type, COUNT(program_type) AS types_count FROM `%s`
			GROUP BY program_type
			""", channelToTableName(slug)));
		final Map<String, Long> programTypes = new HashMap<>();
		if (!channelSlugs.isEmpty()) {
			jdbcTemplate.query(sql, (rs, rowNum) -> {
				programTypes.put(rs.getString("type"), rs.getLong("types_count"));
				return null;
			});
		}
		return programTypes;
	}

	@Override
	public Map<String, String> getAllTvChannels(String query) {
		final String sql = """
			SELECT slug, name FROM tv_channels
			WHERE LOWER(name) LIKE CONCAT('%', LOWER(?), '%')
			""";
		final Map<String, String> tvChannels = new HashMap<>();
		jdbcTemplate.query(sql, (rs, rowNum) -> {
			tvChannels.put(rs.getString("slug"), rs.getString("name"));
			return null;
		}, query);
		return tvChannels;
	}

	@Override
	public Map<String, Long> getChannelsPersistedDays(List<String> channelSlugs) {
		final String sql = unionWrapper(channelSlugs, slug -> String.format("""
			SELECT '%s' as slug, COUNT(DISTINCT DATE(schedule_date)) AS dates_count
			FROM `%s`
			""", slug, channelToTableName(slug)));
		final Map<String, Long> persistedDays = new HashMap<>();
		if (!channelSlugs.isEmpty()) {
			jdbcTemplate.query(sql, (rs, rowNum) -> {
				persistedDays.put(rs.getString("slug"), rs.getLong("dates_count"));
				return null;
			});
		}
		return persistedDays;
	}

	@Override
	public SearchResultDto fullTextPageableSearch(
		List<String> channelSlugs,
		SearchFilterReqDto reqDto,
		int page,
		int pageSize
	) {
		final List<String> filter = new ArrayList<>();
		final Map<String, Object> params = new HashMap<>();

		params.put("searchPhrase", "%" + reqDto.getSearchPhrase() + "%");
		params.put("limit", pageSize);
		params.put("offset", (page - 1) * pageSize);

		String textSearch = "WHERE LOWER(d.name) LIKE LOWER(:searchPhrase)";
		if (reqDto.isFullTextSearch()) {
			textSearch = "WHERE (LOWER(d.name) LIKE LOWER(:searchPhrase) OR LOWER(description) LIKE LOWER(:searchPhrase))";
		}
		filter.add(textSearch);

		if (!reqDto.isTvShowsActive()) {
			filter.add("(episode IS NULL AND season IS NULL)");
		}
		if (!reqDto.getSelectedTvChannels().isEmpty()) {
			filter.add("c.slug IN (:selectedTvChannels)");
			params.put("selectedTvChannels", reqDto.getSelectedTvChannels());
		}
		if (!reqDto.getSelectedProgramTypes().isEmpty()) {
			filter.add("program_type IN (:selectedProgramTypes)");
			params.put("selectedProgramTypes", reqDto.getSelectedProgramTypes());
		}
		if (reqDto.isTvShowsActive() && reqDto.getSeason() != null) {
			filter.add("(season = :season OR season IS NULL)");
			params.put("season", reqDto.getSeason());
		}
		if (reqDto.isTvShowsActive() && reqDto.getEpisode() != null) {
			filter.add("(episode = :episode OR episode IS NULL)");
			params.put("episode", reqDto.getEpisode());
		}
		if (reqDto.getStartDate() != null) {
			filter.add("CAST(CONCAT(schedule_date, ' ', hour_start, ':00') AS DATETIME) > :startDate");
			params.put("startDate", reqDto.getStartDate());
		}
		if (reqDto.getEndDate() != null) {
			filter.add("CAST(CONCAT(schedule_date, ' ', hour_start, ':00') AS DATETIME) < :endDate");
			params.put("endDate", reqDto.getEndDate());
		}
		if (!reqDto.getSelectedWeekdays().isEmpty()) {
			filter.add("weekday IN (:weekdays)");
			params.put("weekdays", reqDto.getSelectedWeekdays());
		}
		final String filterSql = String.join(" AND ", filter);

		final String unionCountSql = unionWrapper(channelSlugs, slug -> String.format("""
			SELECT COUNT(d.id) AS count,
			CEIL(COUNT(d.id) / :limit) AS total_pages
			FROM `%s` AS d
			INNER JOIN tv_channels c ON channel_id = c.id
			%s
			""", channelToTableName(slug), filterSql));

		final String countSql = String.format("""
			SELECT SUM(count) AS count, SUM(total_pages) AS total_pages
			FROM (%s) AS subquery
			""", unionCountSql);

		final PaginationMetadataDto metadata = namedParameterJdbcTemplate
			.queryForObject(countSql, params, new DataClassRowMapper<>(PaginationMetadataDto.class));

		if (metadata == null) {
			throw new RuntimeException("Unable to find pagination metadata.");
		}
		final String unionSearchSql = unionWrapper(channelSlugs, slug -> String.format("""
			SELECT d.name, description, program_type, episode, season, badge, hour_start, schedule_date,
			DAYNAME(schedule_date) AS weekday, c.name AS tv_channel_name
			FROM `%s` AS d
			INNER JOIN tv_channels c ON channel_id = c.id
			%s
			""", channelToTableName(slug), filterSql)
		);
		final String searchSql = String.format("""
			SELECT * FROM (%s) AS subquery
			ORDER BY schedule_date %s LIMIT :limit OFFSET :offset
			""", unionSearchSql, reqDto.isSortNowToPrev() ? "DESC" : "ASC");

		final List<SearchResultElement> elements = namedParameterJdbcTemplate
			.query(searchSql, params, new DataClassRowMapper<>(SearchResultElement.class));

		return new SearchResultDto(elements, reqDto.isTvShowsActive(),
			page, metadata.totalPages(), metadata.count(), pageSize);
	}

	@Override
	public DatabaseCapacityDetailsDto getChannelDatabaseCapacity(String channelSlug) {
		final String sql = String.format("SELECT %s FROM `%s`", getCapacityColumnsSqlStructure(),
			channelToTableName(channelSlug));
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(DatabaseCapacityDetailsDto.class));
	}

	@Override
	public DatabaseCapacityDetailsDto getGlobalDatabaseCapacity(List<String> channelSlugs) {
		final String unionSql = unionWrapper(channelSlugs, slug -> String
			.format("SELECT * FROM `%s`", channelToTableName(slug)));

		final String sql = String.format("SELECT %s FROM (%s) AS subquery", getCapacityColumnsSqlStructure(), unionSql);
		return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(DatabaseCapacityDetailsDto.class));
	}

	private String getCapacityColumnsSqlStructure() {
		return """
			COUNT(DISTINCT DATE(schedule_date)) as persisted_days,
			COUNT(DISTINCT YEAR(schedule_date)) as persisted_years,
			COUNT(id) as persisted_tv_programs,
			SUM(
			   IF(ISNULL(id), 0, 28) +
			   IFNULL(LENGTH(name), 0) +
			   IFNULL(LENGTH(description), 0) +
			   IFNULL(LENGTH(program_type), 0) +
			   IFNULL(LENGTH(badge), 0) +
			   IFNULL(LENGTH(hour_start), 0) +
			   IFNULL(LENGTH(schedule_date), 0) +
			   IFNULL(LENGTH(weekday), 0) +
			   IFNULL(LENGTH(channel_id), 0)
			) AS average_db_size
			""";
	}

	private <T> String unionWrapper(List<T> elements, Function<T, String> callback) {
		boolean firstRow = true;
		final StringJoiner joiner = new StringJoiner(" ");
		for (final T element : elements) {
			if (!firstRow) {
				joiner.add("UNION ALL");
			}
			joiner.add(callback.apply(element));
			firstRow = false;
		}
		return joiner.toString();
	}

	private String channelToTableName(String channelSlug) {
		return "channel_" + channelSlug.replaceAll("-", "_");
	}
}
