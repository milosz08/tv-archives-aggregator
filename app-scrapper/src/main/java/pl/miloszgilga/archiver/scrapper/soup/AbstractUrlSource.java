package pl.miloszgilga.archiver.scrapper.soup;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.miloszgilga.archiver.scrapper.gui.InoperableException;

@Slf4j
abstract class AbstractUrlSource {
  private final BrowserManager browserManager;
  protected Document rootNode;
  protected String url;

  protected AbstractUrlSource(BrowserManager browserManager, UrlSource urlSource, Object... args) {
    this.browserManager = browserManager;
    url = urlSource.getUrl(args);
    if (urlSource.isInstantlyConnection()) {
      connectAndGet();
    }
  }

  public void connectAndGet(String url) {
    log.info("Waiting for response with: {}", url);
    try (final BrowserContext context = browserManager.getBrowser().newContext(
      new Browser.NewContextOptions()
        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
          "(KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"))) {
      final Page page = context.newPage();
      page.navigate(url);

      rootNode = Jsoup.parse(page.content());
      log.debug("Successfully connected to: {}", url);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw new InoperableException(ex);
    }
  }

  public void connectAndGet() {
    connectAndGet(url);
  }
}
