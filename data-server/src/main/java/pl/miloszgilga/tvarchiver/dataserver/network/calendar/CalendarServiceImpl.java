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

package pl.miloszgilga.tvarchiver.dataserver.network.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.network.calendar.dto.CalendarYearDto;
import pl.miloszgilga.tvarchiver.dataserver.util.Constant;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CalendarYearDto> getCalendarStructurePerChannel(String channelSlug) {
        final String yearsSql = """
            SELECT schedule_date FROM tv_programs_data AS pd
            INNER JOIN tv_channels AS c ON pd.channel_id = c.id
            WHERE c.slug = ?
            """;
        final List<LocalDate> dates = jdbcTemplate
            .queryForList(yearsSql, String.class, channelSlug)
            .stream()
            .map(d -> LocalDate.parse(d, Constant.DTF))
            .toList();

        return null;
    }
}
