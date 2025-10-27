package pl.miloszgilga.archiver.scrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TvChannelsSource extends AbstractUrlSource {
  public TvChannelsSource(BrowserManager browserManager) {
    super(browserManager, UrlSource.TV_CHANNELS);
  }

  public List<TvChannel> getAllTvChannels() {
    final Elements channels = rootNode.select("a.atomsTvChannelList__link");
    final List<TvChannel> tvChannels = new ArrayList<>();
    int i = 1;
    for (Element channel : channels) {
      final String name = channel.text();
      final String slug = channel.attr("href").replace(UrlSource.TV_CHANNELS + "/", "");
      tvChannels.add(new TvChannel(i++, name, slug));
    }
    Collections.sort(tvChannels);
    log.info("Fetched {} TV channels", tvChannels.size());
    return tvChannels;
  }
}
