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

package pl.miloszgilga.tvarchiver.dataserver.features.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDayDetailsDto;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public ProgramDayDetailsDto getAllProgramsPerChannelAndDay(String channelSlug, LocalDate day) {
		// fetch channel name
		final String channelNameSql = "SELECT name FROM tv_channels WHERE slug = ?";
		final String channelName = jdbcTemplate.queryForObject(channelNameSql, String.class, channelSlug);
		// fetch channel programs on selected day
		final String channelProgramsSql = """
			SELECT pd.name, description, program_type,
			IF(season IS NULL OR episode IS NULL, false, true) AS isTvShow,
			season, episode, badge, hour_start
			FROM tv_programs_data AS pd
			INNER JOIN tv_channels AS c ON pd.channel_id = c.id
			WHERE c.slug = ? AND schedule_date = ?
			""";
		final List<ProgramDto> programsData = jdbcTemplate.query(channelProgramsSql,
			new DataClassRowMapper<>(ProgramDto.class), channelSlug, day);

		programsData.sort(Comparator.comparing(data -> LocalTime.parse(data.hourStart())));
		return new ProgramDayDetailsDto(channelName, programsData);
	}
}
