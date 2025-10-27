package pl.miloszgilga.archiver.backend.features.tvchannel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.config.AppConfig;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.tvchannel.dto.*;
import pl.miloszgilga.archiver.backend.util.AppUtils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class TvChannelServiceImpl implements TvChannelService {
  private final DataHandler dataHandler;
  private final AppConfig appConfig;

  @Override
  public Map<Character, List<TvChannelResponseDto>> getTvChannelsBySearch(
    String phrase,
    boolean onlyWithSomeData
  ) {
    final Map<String, String> allTvChannels = dataHandler.getAllTvChannels(phrase);
    if (allTvChannels.isEmpty()) {
      return new HashMap<>();
    }
    final List<String> persistedTvChannels = dataHandler.getPersistedChannels();

    final List<String> onlyWithSomeContent = allTvChannels.keySet().stream()
      .filter(persistedTvChannels::contains)
      .toList();

    final Map<String, Long> persistedDays = dataHandler.getChannelsPersistedDays(onlyWithSomeContent);
    final List<TvChannelResponseDto> tvChannels = allTvChannels.entrySet().stream()
      .map(e -> new TvChannelResponseDto(e.getValue(), e.getKey(),
        persistedDays.getOrDefault(e.getKey(), 0L)))
      .toList();

    final var mappedByFirstLetter = new TreeMap<>(tvChannels.stream()
      .filter(channel -> channel.persistedDays() > 0 || !onlyWithSomeData)
      .collect(Collectors.groupingBy(s -> s.name().toLowerCase().charAt(0))));

    final List<TvChannelResponseDto> nonLettersNames = new ArrayList<>();
    final List<Character> elementsToRemoved = new ArrayList<>();

    // filter for non-letter characters
    for (final var entry : mappedByFirstLetter.entrySet()) {
      if (!Character.isLetter(entry.getKey())) {
        nonLettersNames.addAll(entry.getValue());
        elementsToRemoved.add(entry.getKey());
      }
    }
    // removing moved letters
    for (final Character letter : elementsToRemoved) {
      mappedByFirstLetter.remove(letter);
    }
    if (!nonLettersNames.isEmpty()) {
      mappedByFirstLetter.put('#', nonLettersNames); // add saved non-letters tv channels
    }
    // sort all channels in selected letter category
    for (final List<TvChannelResponseDto> channels : mappedByFirstLetter.values()) {
      Collections.sort(channels);
    }
    return mappedByFirstLetter;
  }

  @Override
  public TvChannelDetailsDto getTvChannelDetails(String channelSlug) {
    return dataHandler.getChannelDetails(channelSlug);
  }

  @Override
  public MonthlyProgramsChartDto getMonthlyChannelPrograms(String channelSlug, int year) {
    // fetch all persisted months for selected year in channel
    final List<Integer> persistedMonthsRaw = dataHandler.getPersistedMonths(channelSlug, year);

    final List<String> persistedMonths = persistedMonthsRaw
      .stream()
      .map(monthId -> Month.of(monthId).getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
      .toList();

    // get all program types combined with freq for all months separately
    final List<ProgramTypeDto> programTypesPerYear = dataHandler
      .getProgramTypesPerYear(channelSlug, year);

    // flatted to program types
    final List<String> programTypes = programTypesPerYear.stream()
      .map(ProgramTypeDto::programType)
      .distinct()
      .toList();

    // create map with key as program type and value as months
    final Map<String, List<MonthlyData>> programTypesMap = programTypesPerYear.stream()
      .collect(Collectors.groupingBy(ProgramTypeDto::programType,
        Collectors.mapping(MonthlyData::new, Collectors.toList())));

    final String stackKey = UUID.randomUUID().toString();
    final List<MonthlyProgramChartStackElement> tableData = new ArrayList<>(persistedMonths.size());

    for (final String programType : programTypes) {
      final List<MonthlyData> monthlyData = programTypesMap.get(programType);
      if (monthlyData == null) {
        continue;
      }
      final Map<Integer, Integer> monthFreqMap = monthlyData.stream()
        .collect(Collectors.toMap(MonthlyData::monthId, MonthlyData::freq));

      List<MonthlyData> allMonthsData = new ArrayList<>();
      final int firstMonth = Collections.min(persistedMonthsRaw);
      for (int month = firstMonth; month <= firstMonth + persistedMonths.size() - 1; month++) {
        allMonthsData.add(new MonthlyData(month, monthFreqMap.getOrDefault(month, 0)));
      }
      // sort programs freq months from 1 -> 12
      allMonthsData.sort(Comparator.comparing(MonthlyData::monthId));

      final List<Integer> monthsFreq = allMonthsData.stream()
        .map(MonthlyData::freq)
        .toList();

      tableData.add(new MonthlyProgramChartStackElement(programType, monthsFreq));
    }
    tableData.sort(Comparator.comparing(row -> row.getData().stream()
      .mapToInt(Integer::intValue).sum(), Comparator.reverseOrder()));

    final List<MonthlyProgramChartStackElement> series = new ArrayList<>(persistedMonths.size());

    final List<Integer> allChannelRecordsInMonth = AppUtils.reduce2dList(
      persistedMonths.size(),
      tableData.stream()
        .map(MonthlyProgramChartStackElement::getData)
        .toList()
    );
    final int colorsCount = appConfig.getChartColors().size();
    for (int i = 0; i < tableData.size(); i++) {
      final String color = i > colorsCount - 1
        ? appConfig.getDefaultChartColor()
        : appConfig.getChartColors().get(i);

      final MonthlyProgramChartStackElement tableRow = tableData.get(i);
      tableRow.setColor(color);
      if (i < colorsCount) {
        series.add(tableRow);
        tableRow.setExistInChart(true);
      } else if (i == colorsCount) {
        final List<List<Integer>> restOfData = tableData.subList(colorsCount, tableData.size())
          .stream()
          .map(MonthlyProgramChartStackElement::getData)
          .toList();
        series.add(new MonthlyProgramChartStackElement("Others",
          AppUtils.reduce2dList(persistedMonths.size(), restOfData),
          appConfig.getDefaultChartColor(), true));
      }
    }
    series.sort(Comparator.comparing(row -> row.getData().stream().mapToInt(Integer::intValue).sum(),
      Comparator.reverseOrder()));

    return new MonthlyProgramsChartDto(
      series, tableData, persistedMonths, stackKey, allChannelRecordsInMonth
    );
  }
}
