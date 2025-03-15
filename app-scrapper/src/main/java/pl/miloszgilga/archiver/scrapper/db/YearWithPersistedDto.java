package pl.miloszgilga.archiver.scrapper.db;

public record YearWithPersistedDto(
  int year,
  long count
) {
}
