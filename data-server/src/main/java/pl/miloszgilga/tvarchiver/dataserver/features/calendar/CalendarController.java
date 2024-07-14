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

package pl.miloszgilga.tvarchiver.dataserver.features.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.miloszgilga.tvarchiver.dataserver.features.calendar.dto.CalendarMonthDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
class CalendarController {
	private final CalendarService calendarService;

	@GetMapping("/struct/channel/{channelSlug}/year/{year}")
	ResponseEntity<List<CalendarMonthDto>> getCalendarStructurePerChannel(
		@PathVariable String channelSlug,
		@PathVariable int year
	) {
		return ResponseEntity.ok(calendarService.getCalendarStructurePerChannel(channelSlug, year));
	}

	@GetMapping("/years/channel/{channelSlug}")
	ResponseEntity<List<String>> getChannelPersistedYears(@PathVariable String channelSlug) {
		return ResponseEntity.ok(calendarService.getChannelPersistedYears(channelSlug));
	}
}
