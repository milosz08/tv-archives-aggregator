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
import pl.miloszgilga.tvarchiver.webscrapper.util.Constant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TvChannelDayScheduleSource extends AbstractUrlSource {
	public TvChannelDayScheduleSource(String channelSlug) {
		super(UrlSource.TV_CHANNEL_DAY_SCHEDULE, channelSlug);
	}

	public List<DayScheduleDetails> fetchDayScheduleDetails(LocalDate selectedDay) {
		final String urlWithDate = url + "?dzien=" + Constant.DTF.format(selectedDay);
		connectAndGet(urlWithDate);

		// get all program blocks
		final Elements programsBlocks = rootNode.select(".atomsTvChannelEmissionTile__tileTag");

		final List<DayScheduleDetails> dayScheduleDetails = new ArrayList<>();
		for (final Element programBlock : programsBlocks) {

			final Element titleNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__title");
			final Element descriptionNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__lead");
			final Element programType = programBlock.selectFirst(".atomsTvChannelEmissionTile__category");

			final Element season = programBlock.selectFirst(".atomsTvChannelEmissionTile__season-value");
			final Element episode = programBlock.selectFirst(".atomsTvChannelEmissionTile__episode-value");

			final Element badge = programBlock.selectFirst(".atomsPartialLabelLabelFill__wrapper");
			final Element timeNode = programBlock.selectFirst(".atomsTvChannelEmissionTile__emissionStartDate");

			if (titleNode == null || programType == null || timeNode == null) {
				continue; // skipping for nullable values
			}
			dayScheduleDetails.add(new DayScheduleDetails(
				titleNode.text(),
				descriptionNode == null ? null : descriptionNode.text(),
				programType.text(),
				season == null ? null : Integer.valueOf(season.text()),
				episode == null ? null : Integer.valueOf(episode.text()),
				badge == null ? null : badge.text(),
				timeNode.text()
			));
		}
		return dayScheduleDetails;
	}
}
