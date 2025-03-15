package pl.miloszgilga.archiver.backend.features.search;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchFilterReqDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SearchResultDto;
import pl.miloszgilga.archiver.backend.features.search.dto.SelectRecordDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
class SearchController {
  private final SearchService searchService;

  @GetMapping("/channels")
  ResponseEntity<List<SelectRecordDto>> getTvChannels() {
    return ResponseEntity.ok(searchService.getTvChannels());
  }

  @GetMapping("/program/types")
  ResponseEntity<List<SelectRecordDto>> getProgramTypes() {
    return ResponseEntity.ok(searchService.getProgramTypes());
  }

  @GetMapping("/weekdays")
  ResponseEntity<List<SelectRecordDto>> getWeekdays() {
    return ResponseEntity.ok(searchService.getWeekdays());
  }

  @PostMapping
  ResponseEntity<SearchResultDto> performSearch(
    @RequestBody @Valid SearchFilterReqDto reqDto,
    @RequestParam("page") int page,
    @RequestParam("pageSize") int pageSize
  ) {
    return ResponseEntity.ok(searchService.performSearch(reqDto, page, pageSize));
  }
}
