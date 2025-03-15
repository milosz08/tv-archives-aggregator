package pl.miloszgilga.archiver.scrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.miloszgilga.archiver.scrapper.util.Constant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TvChannelDayScheduleSource extends AbstractUrlSource {
  public TvChannelDayScheduleSource(String channelSlug) {
    super(UrlSource.TV_CHANNEL_DAY_SCHEDULE, channelSlug);
  }

  public List<DayScheduleDetails> fetchDayScheduleDetails(LocalDate selectedDay) {
    final String urlWithDate = url + "?dzien=" + Constant.DTF.format(selectedDay);
    connectAndGet(urlWithDate);

    // get all program blocks
    final Elements programsBlocks = rootNode.select(".atomsTvChannelEmissionTile__tileTag");

    final List<DayScheduleDetails> dayScheduleDetails = new ArrayList<>();
    for (final Element programBlock : programsBlocks) {

      final Element titleNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__title");
      final Element descriptionNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__lead");
      final Element programType = programBlock.selectFirst(".atomsTvChannelEmissionTile__category");

      final Element season = programBlock.selectFirst(".atomsTvChannelEmissionTile__season-value");
      final Element episode = programBlock.selectFirst(".atomsTvChannelEmissionTile__episode-value");

      final Element badge = programBlock.selectFirst(".atomsPartialLabelLabelFill__wrapper");
      final Element timeNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__emissionStartDate");

      if (titleNode == null || programType == null || timeNode == null) {
        continue; // skipping for nullable values
      }
      dayScheduleDetails.add(new DayScheduleDetails(
        titleNode.text(),
        descriptionNode == null ? null : descriptionNode.text(),
        programType.text(),
        season == null ? null : Integer.valueOf(season.text()),
        episode == null ? null : Integer.valueOf(episode.text()),
        badge == null ? null : badge.text(),
        timeNode.text()
      ));
    }
    return dayScheduleDetails;
  }
}
