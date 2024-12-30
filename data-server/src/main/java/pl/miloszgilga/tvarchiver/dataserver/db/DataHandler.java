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

import pl.miloszgilga.tvarchiver.dataserver.features.calendar.dto.MinMaxDateDto;
import pl.miloszgilga.tvarchiver.dataserver.features.program.dto.ProgramDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchResultDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SelectRecordDto;
import pl.miloszgilga.tvarchiver.dataserver.features.tvchannel.dto.ProgramTypeDto;
import pl.miloszgilga.tvarchiver.dataserver.features.tvchannel.dto.TvChannelDetailsDto;
import pl.miloszgilga.tvarchiver.dataserver.features.util.dto.DatabaseCapacityDetailsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DataHandler {
	MinMaxDateDto getMinAndMaxDate(String channelSlug, int year);

	List<String> getChannelPersistedYears(String channelSlug);

	String getChannelName(String channelSlug);

	List<ProgramDto> getChannelProgramForDay(String channelSlug, LocalDate day);

	TvChannelDetailsDto getChannelDetails(String channelSlug);

	List<Integer> getPersistedMonths(String channelSlug, int year);

	List<ProgramTypeDto> getProgramTypesPerYear(String channelSlug, int year);

	List<SelectRecordDto> getChannelsWithAnyContent();

	List<String> getPersistedChannels();

	Map<String, Long> getProgramTypes(List<String> channelSlugs);

	Map<String, String> getAllTvChannels(String query);

	Map<String, Long> getChannelsPersistedDays(List<String> channelSlugs);

	SearchResultDto fullTextPageableSearch(List<String> channelSlugs, SearchFilterReqDto reqDto, int page, int pageSize);

	DatabaseCapacityDetailsDto getChannelDatabaseCapacity(String channelSlug);

	DatabaseCapacityDetailsDto getGlobalDatabaseCapacity(List<String> channelSlugs);
}
