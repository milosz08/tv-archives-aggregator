package pl.miloszgilga.archiver.backend.features.search.dto;

public record SearchResultElement(
  String name,
  String description,
  String programType,
  Integer episode,
  Integer season,
  String badge,
  String hourStart,
  String scheduleDate,
  String weekday,
  String tvChannelName
) {
}
