package pl.miloszgilga.archiver.backend.features.calendar;

import pl.miloszgilga.archiver.backend.features.calendar.dto.CalendarMonthDto;

import java.util.List;

interface CalendarService {
  List<CalendarMonthDto> getCalendarStructurePerChannel(String channelSlug, int year);

  List<String> getChannelPersistedYears(String channelSlug);
}
