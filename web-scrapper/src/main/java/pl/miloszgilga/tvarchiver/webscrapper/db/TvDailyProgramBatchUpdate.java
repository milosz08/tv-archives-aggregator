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

package pl.miloszgilga.tvarchiver.webscrapper.db;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import pl.miloszgilga.tvarchiver.webscrapper.soup.DayScheduleDetails;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class TvDailyProgramBatchUpdate implements BatchPreparedStatementSetter {
	private final List<DayScheduleDetails> dayScheduleDetails;
	private final LocalDate scheduleDate;
	private final Long channelId;

	@Override
	public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
		final DayScheduleDetails details = dayScheduleDetails.get(i);
		ps.setString(1, details.name());
		ps.setObject(2, details.description());
		ps.setString(3, details.programType());
		ps.setObject(4, details.season());
		ps.setObject(5, details.episode());
		ps.setObject(6, details.badge());
		ps.setString(7, details.hourStart());
		ps.setDate(8, Date.valueOf(scheduleDate));
		ps.setInt(9, scheduleDate.getDayOfWeek().getValue());
		ps.setLong(10, channelId);
	}

	@Override
	public int getBatchSize() {
		return dayScheduleDetails.size();
	}
}
