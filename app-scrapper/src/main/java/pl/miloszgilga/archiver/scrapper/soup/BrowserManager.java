package pl.miloszgilga.archiver.scrapper.soup;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.gui.InoperableException;

import java.io.Closeable;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BrowserManager implements Closeable {
  private final Playwright playwright;
  @Getter
  private final Browser browser;

  public BrowserManager() {
    try {
      String appDir = System.getProperty("jpackage.app-dir");
      if (appDir == null) {
        appDir = new File(".pw").getCanonicalPath();
      }
      final String browserPath = Paths.get(appDir, "pw-browsers").toString();

      final Map<String, String> env = new HashMap<>();
      env.put("PLAYWRIGHT_BROWSERS_PATH", browserPath);

      log.info("Start launching browser manager process...");
      log.info("Browsers paths: {}", browserPath);

      final Playwright.CreateOptions options = new Playwright.CreateOptions().setEnv(env);
      playwright = Playwright.create(options);
      browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions()
          .setArgs(List.of(
            "--headless=new",
            "--disable-gpu",
            "--disable-blink-features=AutomationControlled"
          ))
      );
      log.info("Configured browser manager process");
    } catch (Exception ex) {
      throw new InoperableException(ex);
    }
  }

  @Override
  public void close() {
    browser.close();
    playwright.close();
    log.info("Closed browser manager process");
  }
}
