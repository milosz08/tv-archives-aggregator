package pl.miloszgilga.archiver.backend.features.tvchannel;

import pl.miloszgilga.archiver.backend.features.tvchannel.dto.MonthlyProgramsChartDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.TvChannelDetailsDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.TvChannelResponseDto;

import java.util.List;
import java.util.Map;

interface TvChannelService {
  Map<Character, List<TvChannelResponseDto>> getTvChannelsBySearch(
    String phrase,
    boolean onlyWithSomeData
  );

  TvChannelDetailsDto getTvChannelDetails(String channelSlug);

  MonthlyProgramsChartDto getMonthlyChannelPrograms(String channelSlug, int year);
}
