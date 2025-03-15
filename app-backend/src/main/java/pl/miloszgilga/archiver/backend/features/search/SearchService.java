package pl.miloszgilga.archiver.backend.features.search;

import pl.miloszgilga.archiver.backend.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchResultDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SelectRecordDto;

import java.util.List;

interface SearchService {
  List<SelectRecordDto> getTvChannels();

  List<SelectRecordDto> getProgramTypes();

  List<SelectRecordDto> getWeekdays();

  SearchResultDto performSearch(SearchFilterReqDto reqDto, int page, int pageSize);
}
