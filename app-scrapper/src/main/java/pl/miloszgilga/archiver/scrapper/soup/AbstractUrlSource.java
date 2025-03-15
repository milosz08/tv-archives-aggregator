package pl.miloszgilga.archiver.scrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.miloszgilga.archiver.scrapper.gui.InoperableException;

import java.io.IOException;

@Slf4j
abstract class AbstractUrlSource {
  protected Document rootNode;
  protected String url;

  protected AbstractUrlSource(UrlSource urlSource, Object... args) {
    url = urlSource.getUrl(args);
    if (urlSource.isInstantlyConnection()) {
      connectAndGet();
    }
  }

  public void connectAndGet(String url) {
    try {
      rootNode = Jsoup.connect(url)
        .userAgent("Mozilla")
        .get();
      log.debug("Successfully connected to: {}", url);
    } catch (IOException ex) {
      throw new InoperableException(ex);
    }
  }

  public void connectAndGet() {
    connectAndGet(url);
  }
}
