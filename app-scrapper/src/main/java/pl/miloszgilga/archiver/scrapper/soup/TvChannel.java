package pl.miloszgilga.archiver.scrapper.soup;

import org.apache.commons.lang3.StringUtils;

public record TvChannel(
  long id,
  String name,
  String slug
) implements Comparable<TvChannel> {

  public TvChannel() {
    this(0, StringUtils.EMPTY, StringUtils.EMPTY);
  }

  @Override
  public int compareTo(TvChannel o) {
    return name.compareTo(o.name);
  }

  @Override
  public String toString() {
    return name;
  }
}
