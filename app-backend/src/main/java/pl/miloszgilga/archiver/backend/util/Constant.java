package pl.miloszgilga.archiver.backend.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
  public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
