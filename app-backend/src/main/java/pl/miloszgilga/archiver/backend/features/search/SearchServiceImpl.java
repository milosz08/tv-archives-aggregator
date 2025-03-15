package pl.miloszgilga.archiver.backend.features.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchResultDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SelectRecordDto;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
class SearchServiceImpl implements SearchService {
  private final DataHandler dataHandler;

  @Override
  public List<SelectRecordDto> getTvChannels() {
    return dataHandler.getChannelsWithAnyContent();
  }

  @Override
  public List<SelectRecordDto> getProgramTypes() {
    final List<String> channelSlugs = dataHandler.getPersistedChannels();
    final Map<String, Long> programTypes = dataHandler.getProgramTypes(channelSlugs);
    return programTypes.entrySet().stream()
      .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
      .map(entry -> new SelectRecordDto(
        entry.getKey().toLowerCase().replaceAll(" ", "-"),
        String.format("%s (%d)", entry.getKey(), entry.getValue()))
      )
      .toList();
  }

  @Override
  public List<SelectRecordDto> getWeekdays() {
    return Arrays.stream(DayOfWeek.values())
      .map(dayOfWeek -> new SelectRecordDto(Integer.toString(dayOfWeek.getValue()),
        dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)))
      .toList();
  }

  @Override
  public SearchResultDto performSearch(SearchFilterReqDto reqDto, int page, int pageSize) {
    final List<String> persistedChannels = dataHandler.getPersistedChannels();
    return dataHandler.fullTextPageableSearch(persistedChannels, reqDto, page, pageSize);
  }
}
