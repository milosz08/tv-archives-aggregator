package pl.miloszgilga.archiver.backend.features.tvchannel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.MonthlyProgramsChartDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.TvChannelDetailsDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.TvChannelResponseDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tv-channel")
@RequiredArgsConstructor
class TvChannelController {
  private final TvChannelService tvChannelService;

  @GetMapping("/all/search")
  ResponseEntity<Map<Character, List<TvChannelResponseDto>>> getTvChannelsBySearch(
    @RequestParam("phrase") String phrase,
    @RequestParam("onlyWithSomeData") boolean onlyWithSomeData
  ) {
    return ResponseEntity.ok(tvChannelService.getTvChannelsBySearch(phrase, onlyWithSomeData));
  }

  @GetMapping("/details/{channelSlug}")
  ResponseEntity<TvChannelDetailsDto> getChannelDetails(@PathVariable String channelSlug) {
    return ResponseEntity.ok(tvChannelService.getTvChannelDetails(channelSlug));
  }

  @GetMapping("/{channelSlug}/chart/year/{year}/program/types")
  ResponseEntity<MonthlyProgramsChartDto> getMonthlyChannelPrograms(
    @PathVariable String channelSlug,
    @PathVariable int year
  ) {
    return ResponseEntity.ok(tvChannelService.getMonthlyChannelPrograms(channelSlug, year));
  }
}
