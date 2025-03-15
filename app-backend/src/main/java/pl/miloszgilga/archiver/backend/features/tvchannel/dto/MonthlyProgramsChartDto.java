package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

import java.util.List;

public record MonthlyProgramsChartDto(
  List<MonthlyProgramChartStackElement> series,
  List<MonthlyProgramChartStackElement> table,
  List<String> months,
  String stackKey,
  List<Integer> allPerMonths
) {
}
