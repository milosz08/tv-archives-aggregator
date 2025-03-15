package pl.miloszgilga.archiver.scrapper.soup;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TvChannelYearData {
  private final long totalCount;
  private long fetchedCount;
}
