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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SearchResultDto;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.SelectRecordDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
class SearchController {
	private final SearchService searchService;

	@GetMapping("/channels")
	ResponseEntity<List<SelectRecordDto>> getTvChannels() {
		return ResponseEntity.ok(searchService.getTvChannels());
	}

	@GetMapping("/program/types")
	ResponseEntity<List<SelectRecordDto>> getProgramTypes() {
		return ResponseEntity.ok(searchService.getProgramTypes());
	}

	@PostMapping
	ResponseEntity<SearchResultDto> performSearch(
		@RequestBody @Valid SearchFilterReqDto reqDto,
		@RequestParam("page") int page,
		@RequestParam("pageSize") int pageSize
	) {
		return ResponseEntity.ok(searchService.performSearch(reqDto, page, pageSize));
	}
}
