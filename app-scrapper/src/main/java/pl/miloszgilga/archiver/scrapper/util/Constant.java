package pl.miloszgilga.archiver.scrapper.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
  public static final DecimalFormat PF = new DecimalFormat("#.##");
  public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final Locale D_LC = new Locale("pl", "PL");
}
