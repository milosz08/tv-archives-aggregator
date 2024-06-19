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

package pl.miloszgilga.tvarchiver.dataserver.network.program;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.miloszgilga.tvarchiver.dataserver.network.program.dto.ProgramDto;
import pl.miloszgilga.tvarchiver.dataserver.pageable.PageableDto;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/channel")
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;

    @GetMapping("/all/channel/{channelSlug}")
    ResponseEntity<PageableDto<ProgramDto>> getPageableProgramsByTvChannelAndDate(
        @PathVariable String channelSlug,
        @RequestParam("date") LocalDate date
    ) {
        return ResponseEntity.ok(programService.getAllProgramsPerChannelAndDay(channelSlug, date));
    }
}
