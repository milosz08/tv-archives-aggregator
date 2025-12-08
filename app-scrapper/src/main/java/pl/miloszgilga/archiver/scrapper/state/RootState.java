package pl.miloszgilga.archiver.scrapper.state;

import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import pl.miloszgilga.archiver.scrapper.db.DataHandler;
import pl.miloszgilga.archiver.scrapper.soup.BrowserManager;
import pl.miloszgilga.archiver.scrapper.soup.TvChannel;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelDetails;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RootState extends AbstractDisposableProvider {
  private final BehaviorSubject<List<TvChannel>> tvChannels$;
  private final BehaviorSubject<TvChannel> selectedChannel$;
  private final BehaviorSubject<AppState> appState$;
  private final BehaviorSubject<Integer> randomness$;
  private final BehaviorSubject<Long> totalFetchedCount$;
  private final BehaviorSubject<TvChannelDetails> channelDetails$;
  private final BehaviorSubject<Integer> selectedYear$;

  private Dotenv dotenv;
  @Getter
  private DataHandler dataHandler;
  @Getter
  private BrowserManager browserManager;

  public RootState(BrowserManager browserManager) {
    this.browserManager = browserManager;
    try {
      dotenv = Dotenv.load();
    } catch (Exception ignored) {
    }
    tvChannels$ = BehaviorSubject.createDefault(new ArrayList<>());
    selectedChannel$ = BehaviorSubject.createDefault(new TvChannel());
    appState$ = BehaviorSubject.createDefault(AppState.IDLE);
    randomness$ = BehaviorSubject.createDefault(5);
    totalFetchedCount$ = BehaviorSubject.createDefault(0L);
    channelDetails$ = BehaviorSubject.create();
    selectedYear$ = BehaviorSubject.createDefault(-1);
  }

  public void updateTvChannels(List<TvChannel> channels) {
    this.tvChannels$.onNext(channels);
  }

  public void updateSelectedChannel(TvChannel channel) {
    this.selectedChannel$.onNext(channel);
  }

  public void updateAppState(AppState state) {
    this.appState$.onNext(state);
  }

  public void updateRandomness(int randomness) {
    this.randomness$.onNext(randomness);
  }

  public void updateTotalFetchedCount(long totalFetchedCount) {
    this.totalFetchedCount$.onNext(totalFetchedCount);
  }

  public void updateChannelDetails(TvChannelDetails details) {
    this.channelDetails$.onNext(details);
  }

  public void updateSelectedYear(int year) {
    this.selectedYear$.onNext(year);
  }

  public Observable<List<TvChannel>> getTvChannels$() {
    return this.tvChannels$.hide();
  }

  public Observable<TvChannel> getSelectedChannel$() {
    return this.selectedChannel$.hide();
  }

  public Observable<AppState> getAppState$() {
    return this.appState$.hide();
  }

  public Observable<Long> getTotalFetchedCount$() {
    return this.totalFetchedCount$.hide();
  }

  public Observable<TvChannelDetails> getChannelDetails$() {
    return this.channelDetails$.hide();
  }

  public Observable<Integer> getSelectedYear$() {
    return this.selectedYear$.hide();
  }

  public List<TvChannel> getTvChannels() {
    return this.tvChannels$.getValue();
  }

  public TvChannel getSelectedChannel() {
    return this.selectedChannel$.getValue();
  }

  public Integer getRandomness() {
    return this.randomness$.getValue();
  }

  public String getEnvValue(EnvKey key) {
    return dotenv == null ? key.getDefaultValue() : dotenv.get(key.name(), key.getDefaultValue());
  }

  public Integer getSelectedYear() {
    return this.selectedYear$.getValue();
  }

  public TvChannelDetails getTvChannelDetails() {
    return this.channelDetails$.getValue();
  }

  public Long getTotalFetchedCount() {
    return this.totalFetchedCount$.getValue();
  }

  @SneakyThrows
  @Override
  public void onCleanup() {
    if (dataHandler != null) {
      dataHandler.close();
    }
    browserManager.close();
  }
}
