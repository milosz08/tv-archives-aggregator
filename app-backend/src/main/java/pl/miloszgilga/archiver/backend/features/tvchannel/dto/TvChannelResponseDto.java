package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

public record TvChannelResponseDto(
  String name,
  String slug,
  long persistedDays
) implements Comparable<TvChannelResponseDto> {
  @Override
  public int compareTo(TvChannelResponseDto o) {
    return o.name.compareTo(name);
  }
}
