package pl.miloszgilga.archiver.backend.features.program;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.miloszgilga.archiver.backend.features.program.dto.ProgramDayDetailsDto;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/program")
@RequiredArgsConstructor
class ProgramController {
  private final ProgramService programService;

  @GetMapping("/all/channel/{channelSlug}/date/{date}")
  ResponseEntity<ProgramDayDetailsDto> getAllProgramsPerChannelAndDay(
    @PathVariable String channelSlug,
    @PathVariable LocalDate date
  ) {
    return ResponseEntity.ok(programService.getAllProgramsPerChannelAndDay(channelSlug, date));
  }
}
