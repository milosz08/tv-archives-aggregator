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

package pl.miloszgilga.tvarchiver.dataserver.features.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.features.util.dto.DatabaseCapacityDetailsDto;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
class UtilServiceImpl implements UtilService {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public DatabaseCapacityDetailsDto getDatabaseCapacityDetails(String channelSlug) {
		final boolean globalCapacity = StringUtils.equals(channelSlug, StringUtils.EMPTY);
		final String distinctSelector = globalCapacity ? ", channel_id" : StringUtils.EMPTY;

		final String sql = String.format("""
				SELECT
					COUNT(DISTINCT DATE(schedule_date)%s) as persisted_days,
					COUNT(DISTINCT YEAR(schedule_date)%s) as persisted_years,
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
				FROM tv_channels AS c LEFT JOIN tv_programs_data AS d ON d.channel_id = c.id %s
				""",
			distinctSelector,
			distinctSelector,
			globalCapacity ? "" : "WHERE slug = :channelSlug GROUP BY c.id"
		);

		final Map<String, Object> params = new HashMap<>();
		if (!globalCapacity) {
			params.put("channelSlug", channelSlug);
		}
		return namedParameterJdbcTemplate
			.queryForObject(sql, params, new DataClassRowMapper<>(DatabaseCapacityDetailsDto.class));
	}
}
