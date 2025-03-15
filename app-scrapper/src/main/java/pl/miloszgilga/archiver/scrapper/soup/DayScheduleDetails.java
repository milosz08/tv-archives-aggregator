package pl.miloszgilga.archiver.scrapper.soup;

import java.io.Serializable;

public record DayScheduleDetails(
  String name,
  String description,
  String programType,
  Integer season,
  Integer episode,
  String badge,
  String hourStart
) implements Serializable {
}
