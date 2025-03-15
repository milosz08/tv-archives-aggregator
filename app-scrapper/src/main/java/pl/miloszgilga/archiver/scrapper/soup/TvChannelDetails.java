package pl.miloszgilga.archiver.scrapper.soup;

import java.time.LocalDate;
import java.util.TreeMap;

public record TvChannelDetails(
  TreeMap<Integer, TvChannelYearData> years,
  LocalDate startDate,
  LocalDate endDate,
  long daysCount
) {
}
