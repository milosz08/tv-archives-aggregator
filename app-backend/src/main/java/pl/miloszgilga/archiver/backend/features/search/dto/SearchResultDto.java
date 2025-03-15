package pl.miloszgilga.archiver.backend.features.search.dto;

import java.util.List;

public record SearchResultDto(
  List<SearchResultElement> elements,
  boolean viewTvShowColumn,
  int page,
  int totalPages,
  int totalElements,
  int perPage
) {
}
