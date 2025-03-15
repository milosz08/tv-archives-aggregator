package pl.miloszgilga.archiver.backend.features.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.miloszgilga.archiver.backend.features.calendar.dto.CalendarMonthDto;

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
