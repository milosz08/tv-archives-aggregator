package pl.miloszgilga.archiver.backend.features.calendar.dto;

import java.time.LocalDate;

public record MinMaxDateDto(
  LocalDate start,
  LocalDate end
) {
}
