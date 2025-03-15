package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

public record MonthlyData(
  Integer monthId,
  Integer freq
) {
  public MonthlyData(ProgramTypeDto programTypeDto) {
    this(programTypeDto.monthId(), programTypeDto.countPerMonth());
  }
}
