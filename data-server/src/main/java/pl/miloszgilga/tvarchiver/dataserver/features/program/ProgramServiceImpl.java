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
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.db.DataHandler;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDayDetailsDto;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class ProgramServiceImpl implements ProgramService {
	private final DataHandler dataHandler;

	@Override
	public ProgramDayDetailsDto getAllProgramsPerChannelAndDay(String channelSlug, LocalDate day) {
		// fetch channel name
		final String channelName = dataHandler.getChannelName(channelSlug);
		// fetch channel programs on selected day
		final List<ProgramDto> programsData = dataHandler.getChannelProgramForDay(channelSlug, day);
		programsData.sort(Comparator.comparing(data -> LocalTime.parse(data.hourStart())));
		return new ProgramDayDetailsDto(channelName, programsData);
	}
}
