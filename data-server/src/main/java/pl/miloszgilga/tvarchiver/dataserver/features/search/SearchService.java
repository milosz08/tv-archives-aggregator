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

import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchResultDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SelectRecordDto;

import java.util.List;

interface SearchService {
	List<SelectRecordDto> getTvChannels();

	List<SelectRecordDto> getProgramTypes();

	List<SelectRecordDto> getWeekdays();

	SearchResultDto performSearch(SearchFilterReqDto reqDto, int page, int pageSize);
}
