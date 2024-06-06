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

package pl.miloszgilga.tvarchiver.webscrapper.scrap;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import java.util.Random;

@Slf4j
public class DataScrapperThread extends Thread {
	private final RootState rootState;
	private final TvChannel selectedChannel;
	private final long minDelayMs;
	private final long maxDelayMs;
	private final Random random;

	private boolean isScrappingActive = true;
	private long processRecords = 0;

	public DataScrapperThread(RootState rootState, int minDelay) {
		super("ScrapperThread");
		this.rootState = rootState;
		this.selectedChannel = rootState.getSelectedChannel();
		minDelayMs = minDelay * 1000L;
		maxDelayMs = rootState.getRandomness() * 1000L;
		random = new Random();
	}

	@Override
	public void run() {
		// init load data from db and prepare scrapper function
		while (isScrappingActive) {
			final long randomnessWaitingTime = minDelayMs + random.nextLong(maxDelayMs - minDelayMs + 1);
			log.info("Scrapping data... Iteration: {}, randomness: {}ms", ++processRecords, randomnessWaitingTime);
			rootState.updateProgressBar((int) processRecords);
			try {
				sleep(randomnessWaitingTime);
			} catch (InterruptedException ex) {
				log.error("Scrapping thread interrupted. Cause: {}", ex.getMessage());
				break;
			}
		}
		log.info("Stopped scrap data from: {} with processed records count: {}", selectedChannel, processRecords);
	}

	@Override
	public synchronized void start() {
		log.info("Staring scrap data from channel: {} with max delay: {}s", selectedChannel, maxDelayMs / 1000);
		super.start();
	}

	public void stopScrapping() {
		isScrappingActive = false;
	}
}
