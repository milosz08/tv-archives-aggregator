package pl.miloszgilga.archiver.backend.features.program.dto;

public record ProgramDto(
  String name,
  String description,
  String programType,
  boolean isTvShow,
  Integer season,
  Integer episode,
  String badge,
  String hourStart
) {
}
