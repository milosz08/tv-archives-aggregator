package pl.miloszgilga.archiver.scrapper.db;

import pl.miloszgilga.archiver.scrapper.soup.DayScheduleDetails;
import pl.miloszgilga.archiver.scrapper.soup.TvChannel;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.List;

public interface DataHandler extends Closeable {
  InetSocketAddress getDbHost();

  List<TvChannel> getTvChannels();

  void batchInsertTvChannels(List<TvChannel> tvChannels);

  void createChannelTableIfNotExists(String channelSlug);

  List<LocalDate> getAlreadySavedDates(String channelSlug, LocalDate start, LocalDate end);

  void batchInsertChannelData(
    TvChannel tvChannel,
    List<DayScheduleDetails> dayScheduleDetails,
    LocalDate scheduleDate
  );

  List<YearWithPersistedDto> getAlreadyPersistedPerYear(String channelSlug);

  Long getPersistedProgramsPerYear(String channelSlug);

  int deleteChannelDataByYear(String channelSlug, int year);

  Long getDatabaseSize();
}
