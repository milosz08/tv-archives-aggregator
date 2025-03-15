package pl.miloszgilga.archiver.backend.db;

import pl.miloszgilga.archiver.backend.features.calendar.dto.MinMaxDateDto;
import pl.miloszgilga.archiver.backend.features.program.dto.ProgramDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchResultDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SelectRecordDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.ProgramTypeDto;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.TvChannelDetailsDto;
import pl.miloszgilga.archiver.backend.features.util.dto.DatabaseCapacityDetailsDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DataHandler {
  MinMaxDateDto getMinAndMaxDate(String channelSlug, int year);

  List<String> getChannelPersistedYears(String channelSlug);

  String getChannelName(String channelSlug);

  List<ProgramDto> getChannelProgramForDay(String channelSlug, LocalDate day);

  TvChannelDetailsDto getChannelDetails(String channelSlug);

  List<Integer> getPersistedMonths(String channelSlug, int year);

  List<ProgramTypeDto> getProgramTypesPerYear(String channelSlug, int year);

  List<SelectRecordDto> getChannelsWithAnyContent();

  List<String> getPersistedChannels();

  Map<String, Long> getProgramTypes(List<String> channelSlugs);

  Map<String, String> getAllTvChannels(String query);

  Map<String, Long> getChannelsPersistedDays(List<String> channelSlugs);

  SearchResultDto fullTextPageableSearch(
    List<String> channelSlugs,
    SearchFilterReqDto reqDto,
    int page,
    int pageSize
  );

  DatabaseCapacityDetailsDto getChannelDatabaseCapacity(String channelSlug);

  DatabaseCapacityDetailsDto getGlobalDatabaseCapacity(List<String> channelSlugs);
}
