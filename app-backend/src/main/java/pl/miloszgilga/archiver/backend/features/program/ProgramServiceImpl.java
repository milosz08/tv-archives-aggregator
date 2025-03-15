package pl.miloszgilga.archiver.backend.features.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.program.dto.ProgramDayDetailsDto;
import pl.miloszgilga.archiver.backend.features.program.dto.ProgramDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class ProgramServiceImpl implements ProgramService {
  private final DataHandler dataHandler;

  @Override
  public ProgramDayDetailsDto getAllProgramsPerChannelAndDay(String channelSlug, LocalDate day) {
    // fetch channel name
    final String channelName = dataHandler.getChannelName(channelSlug);
    // fetch channel programs on selected day
    final List<ProgramDto> programsData = dataHandler.getChannelProgramForDay(channelSlug, day);
    programsData.sort(Comparator.comparing(data -> LocalTime.parse(data.hourStart())));
    return new ProgramDayDetailsDto(channelName, programsData);
  }
}
