package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

public record ProgramTypeDto(
  String programType,
  Integer countPerMonth,
  Integer monthId
) {
}
