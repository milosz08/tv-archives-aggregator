package pl.miloszgilga.archiver.backend.features.program;

import pl.miloszgilga.archiver.backend.features.program.dto.ProgramDayDetailsDto;

import java.time.LocalDate;

interface ProgramService {
  ProgramDayDetailsDto getAllProgramsPerChannelAndDay(String channelSlug, LocalDate day);
}
