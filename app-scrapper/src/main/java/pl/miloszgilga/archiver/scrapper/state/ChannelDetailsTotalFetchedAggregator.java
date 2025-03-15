package pl.miloszgilga.archiver.scrapper.state;

import pl.miloszgilga.archiver.scrapper.soup.TvChannelDetails;

public record ChannelDetailsTotalFetchedAggregator(
  TvChannelDetails details,
  long totalFetched
) {
}
