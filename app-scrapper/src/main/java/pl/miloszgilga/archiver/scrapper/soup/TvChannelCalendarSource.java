package pl.miloszgilga.archiver.scrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.miloszgilga.archiver.scrapper.gui.InoperableException;
import pl.miloszgilga.archiver.scrapper.util.Constant;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class TvChannelCalendarSource extends AbstractUrlSource {
  private final String channelSlug;
  private Elements dateNodes;

  public TvChannelCalendarSource(BrowserManager browserManager, String channelSlug) {
    super(browserManager, UrlSource.TV_CHANNEL_CALENDAR, channelSlug);
    this.channelSlug = channelSlug;
  }

  public TvChannelDetails getSelectedTvChannelDetails() {
    dateNodes = rootNode.select(".componentsTvArchive__year"); // all available years for tv channel
    // map all dates to integer values
    final List<Integer> years = rootNode.select(".componentsTvArchive__year-date").stream()
      .map(Element::text)
      .map(Integer::parseInt)
      .toList();

    // start and end date of tv channel archive
    final LocalDate startDate = findDateBaseCalendarNode(true);
    final LocalDate endDate = findDateBaseCalendarNode(false);

    // fetch all days per single year
    final TreeMap<Integer, TvChannelYearData> fetchedYearsData = new TreeMap<>(Comparator.reverseOrder());
    for (final Integer year : years) {
      LocalDate yearStart = LocalDate.of(year, 1, 1);
      if (yearStart.isBefore(startDate)) {
        yearStart = startDate;
      }
      LocalDate yearEnd = LocalDate.of(year, 12, 31);
      if (yearEnd.isAfter(endDate)) {
        yearEnd = endDate;
      }
      final long daysBetween = daysBetweenInclusive(yearStart, yearEnd);
      fetchedYearsData.put(year, new TvChannelYearData(daysBetween));
    }
    final long daysCount = daysBetweenInclusive(startDate, endDate);
    log.info("Channel: {}. Found: {} days with start: {} and end: {}", channelSlug, daysCount,
      startDate, endDate);
    return new TvChannelDetails(fetchedYearsData, startDate, endDate, daysCount);
  }

  private LocalDate findDateBaseCalendarNode(boolean isStartDate) {
    // get first or last date (from YYYY to YYYY)
    final Element dateNode = dateNodes.get(isStartDate ? dateNodes.size() - 1 : 0);
    final int selectedYear = Integer
      .parseInt(dateNode.select(".componentsTvArchive__year-date").text());

    // for first date, get last month, for end date get first month
    final Elements monthNodes = dateNode.select(".componentsTvArchive__month");
    final Element monthNode = monthNodes.get(isStartDate ? 0 : monthNodes.size() - 1);

    final String monthName = monthNode.select(".componentsTvArchive__month-name").text();
    final Elements monthDayNodes = monthNode.select(".componentsTvArchive__day");

    Month selectedMonth = null;
    for (final Month month : Month.values()) {
      if (month.getDisplayName(TextStyle.FULL_STANDALONE, Constant.D_LC).equalsIgnoreCase(monthName)) {
        selectedMonth = month;
        break;
      }
    }
    if (selectedMonth == null) {
      throw new InoperableException("Could not find selected month");
    }
    // for first date, get last day of last month, for end date get first day of first month
    final int selectedDay = Integer
      .parseInt(monthDayNodes.get(isStartDate ? 0 : monthDayNodes.size() - 1).text());
    return LocalDate.of(selectedYear, selectedMonth, selectedDay);
  }

  private long daysBetweenInclusive(LocalDate start, LocalDate end) {
    return ChronoUnit.DAYS.between(start, end) + 1;
  }
}
