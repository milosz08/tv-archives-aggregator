package pl.miloszgilga.archiver.scrapper.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppIcon {
  DELETE("Delete"),
  DOWNLOAD("Download"),
  PRINT("Print"),
  UPLOAD("Upload"),
  ;

  private final String name;
}
