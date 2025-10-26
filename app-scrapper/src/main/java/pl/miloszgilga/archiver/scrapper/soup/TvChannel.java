package pl.miloszgilga.archiver.scrapper.soup;

import org.jetbrains.annotations.NotNull;

public record TvChannel(
  long id,
  String name,
  String slug
) implements Comparable<TvChannel> {

  public TvChannel() {
    this(0, "", "");
  }

  @Override
  public int compareTo(TvChannel o) {
    return name.compareTo(o.name);
  }

  @Override
  @NotNull
  public String toString() {
    return name;
  }
}
