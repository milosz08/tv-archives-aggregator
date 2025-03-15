package pl.miloszgilga.archiver.backend.features.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.calendar.dto.CalendarDay;
import pl.miloszgilga.archiver.backend.features.calendar.dto.CalendarMonthDto;
import pl.miloszgilga.archiver.backend.features.calendar.dto.MinMaxDateDto;
import pl.miloszgilga.archiver.backend.util.Constant;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class CalendarServiceImpl implements CalendarService {
  private final DataHandler dataHandler;

  @Override
  public List<CalendarMonthDto> getCalendarStructurePerChannel(String channelSlug, int year) {
    final MinMaxDateDto miMaxDate = dataHandler.getMinAndMaxDate(channelSlug, year);
    if (miMaxDate == null || miMaxDate.start() == null || miMaxDate.end() == null) {
      return List.of();
    }
    final List<CalendarMonthDto> months = new ArrayList<>();
    LocalDate currentDate = miMaxDate.start();

    while (!currentDate.isAfter(miMaxDate.end())) {
      final Month month = currentDate.getMonth();

      final CalendarMonthDto monthDto = months.stream()
        .filter(m -> m.name().equals(month.name().toLowerCase()))
        .findFirst()
        .orElseGet(() -> {
          final LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
          final CalendarMonthDto newMonthDto = new CalendarMonthDto(month.name().toLowerCase(),
            firstDayOfMonth.getDayOfWeek().getValue() - 1, new ArrayList<>());
          months.add(newMonthDto);
          return newMonthDto;
        });
      final int dayOfMonth = currentDate.getDayOfMonth();
      monthDto.days().add(new CalendarDay(dayOfMonth, currentDate.format(Constant.DTF)));
      currentDate = currentDate.plusDays(1);
    }
    return months;
  }

  @Override
  public List<String> getChannelPersistedYears(String channelSlug) {
    return dataHandler.getChannelPersistedYears(channelSlug);
  }
}
