package pl.miloszgilga.archiver.scrapper.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnvKey {
  DB_HOST("localhost"),
  DB_PORT("4850"),
  DB_USERNAME("root"),
  DB_PASSWORD("admin"),
  DB_NAME("aggregator-db"),
  ;

  private final String defaultValue;
}
