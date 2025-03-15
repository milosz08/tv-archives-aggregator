package pl.miloszgilga.archiver.scrapper.gui.component;

import pl.miloszgilga.archiver.scrapper.file.FileUtils;
import pl.miloszgilga.archiver.scrapper.gui.AppIcon;

import javax.swing.*;

public class JAppIconButton extends JButton {
  public JAppIconButton(String text, AppIcon iconName, boolean setDescription) {
    if (setDescription) {
      setToolTipText(text);
    } else {
      setText(text);
    }
    FileUtils.getImageIconFromResources(getClass(), iconName).ifPresent(this::setIcon);
  }
}
