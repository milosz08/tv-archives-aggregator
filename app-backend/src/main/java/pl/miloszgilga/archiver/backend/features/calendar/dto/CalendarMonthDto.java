package pl.miloszgilga.archiver.backend.features.calendar.dto;

import java.util.List;

public record CalendarMonthDto(
  String name,
  int countOfEmptyBlocks,
  List<CalendarDay> days
) {
}
