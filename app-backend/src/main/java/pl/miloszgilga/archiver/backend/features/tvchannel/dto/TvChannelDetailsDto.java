package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

public record TvChannelDetailsDto(
  String name,
  boolean hasPersistedDays
) {
}
