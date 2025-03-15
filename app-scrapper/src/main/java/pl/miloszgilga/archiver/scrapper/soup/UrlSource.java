package pl.miloszgilga.archiver.scrapper.soup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UrlSource {
  TV_CHANNELS("https://telemagazyn.pl/stacje", true),
  TV_CHANNEL_CALENDAR("https://telemagazyn.pl/stacje/%s/archiwum", true),
  TV_CHANNEL_DAY_SCHEDULE("https://telemagazyn.pl/stacje/%s", false),
  ;

  private final String url;
  @Getter
  private final boolean instantlyConnection;

  public String getUrl(Object... args) {
    return String.format(url, args);
  }

  @Override
  public String toString() {
    return url;
  }
}
