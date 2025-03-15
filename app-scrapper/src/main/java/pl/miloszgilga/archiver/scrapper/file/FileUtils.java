package pl.miloszgilga.archiver.scrapper.file;

import pl.miloszgilga.archiver.scrapper.gui.AppIcon;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;

public class FileUtils {
  public static Optional<ImageIcon> getImageIconFromResources(Class<?> clazz, AppIcon appIcon) {
    final Optional<URL> iconUrl = getAssetFileFromResources(clazz, "assets/icons/%s.png",
      appIcon.getName());
    return iconUrl.map(ImageIcon::new);
  }

  public static Optional<URL> getAssetFileFromResources(
    Class<?> clazz,
    String resourcePath,
    Object... args
  ) {
    final URL iconUrl = clazz.getResource(String.format("/%s", String.format(resourcePath, args)));
    return Optional.ofNullable(iconUrl);
  }
}
