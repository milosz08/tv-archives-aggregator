package pl.miloszgilga.archiver.backend.features.program.dto;

import java.util.List;

public record ProgramDayDetailsDto(
  String channelName,
  List<ProgramDto> listOfPrograms
) {
}
