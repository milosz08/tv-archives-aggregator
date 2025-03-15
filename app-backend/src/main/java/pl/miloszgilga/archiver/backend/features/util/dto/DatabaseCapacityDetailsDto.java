package pl.miloszgilga.archiver.backend.features.util.dto;

public record DatabaseCapacityDetailsDto(
  long persistedDays,
  long persistedYears,
  long persistedTvPrograms,
  long averageDbSize
) {
}
