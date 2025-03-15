package pl.miloszgilga.archiver.backend.features.search.dto;

public record PaginationMetadataDto(
  int count,
  int totalPages
) {
}
