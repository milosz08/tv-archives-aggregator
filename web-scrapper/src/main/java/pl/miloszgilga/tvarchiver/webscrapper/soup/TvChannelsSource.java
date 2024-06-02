/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.miloszgilga.tvarchiver.webscrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TvChannelsSource extends AbstractUrlSource {
	public TvChannelsSource() {
		super(UrlSource.TV_CHANNELS);
	}

	public List<TvChannel> getAllTvChannels() {
		final Elements channels = rootNode.select("a.atomsTvChannelList__link");
		final List<TvChannel> tvChannels = new ArrayList<>();
		for (Element channel : channels) {
			final String name = channel.text();
			final String slug = channel.attr("href").replace(UrlSource.TV_CHANNELS + "/", "");
			tvChannels.add(new TvChannel(0, name, slug));
		}
		Collections.sort(tvChannels);
		log.info("Fetched {} TV channels", tvChannels.size());
		return tvChannels;
	}
}
