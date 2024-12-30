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

package pl.miloszgilga.tvarchiver.dataserver.features.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.db.DataHandler;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchResultDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SelectRecordDto;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
class SearchServiceImpl implements SearchService {
	private final DataHandler dataHandler;

	@Override
	public List<SelectRecordDto> getTvChannels() {
		return dataHandler.getChannelsWithAnyContent();
	}

	@Override
	public List<SelectRecordDto> getProgramTypes() {
		final List<String> channelSlugs = dataHandler.getPersistedChannels();
		final Map<String, Long> programTypes = dataHandler.getProgramTypes(channelSlugs);
		return programTypes.entrySet().stream()
			.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
			.map(entry -> new SelectRecordDto(
				entry.getKey().toLowerCase().replaceAll(" ", "-"),
				String.format("%s (%d)", entry.getKey(), entry.getValue()))
			)
			.toList();
	}

	@Override
	public List<SelectRecordDto> getWeekdays() {
		return Arrays.stream(DayOfWeek.values())
			.map(dayOfWeek -> new SelectRecordDto(Integer.toString(dayOfWeek.getValue()),
				dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)))
			.toList();
	}

	@Override
	public SearchResultDto performSearch(SearchFilterReqDto reqDto, int page, int pageSize) {
		final List<String> persistedChannels = dataHandler.getPersistedChannels();
		return dataHandler.fullTextPageableSearch(persistedChannels, reqDto, page, pageSize);
	}
}
