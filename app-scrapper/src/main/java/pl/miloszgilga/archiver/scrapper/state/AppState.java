package pl.miloszgilga.archiver.scrapper.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppState {
  IDLE("Idle"),
  SCRAPPING("Scrapping"),
  ;

  private final String state;

  public String createState() {
    return String.format("State: %s", state);
  }

  public boolean isIdle() {
    return this.equals(AppState.IDLE);
  }
}
