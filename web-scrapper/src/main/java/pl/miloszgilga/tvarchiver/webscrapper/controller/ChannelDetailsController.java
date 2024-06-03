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

package pl.miloszgilga.tvarchiver.webscrapper.controller;

import lombok.RequiredArgsConstructor;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ChannelDetailsPanel;
import pl.miloszgilga.tvarchiver.webscrapper.state.AppState;

@RequiredArgsConstructor
public class ChannelDetailsController {
	private final ChannelDetailsPanel channelDetailsPanel;

	public void startScrapping() {
		updateAppState(AppState.SCRAPPING);
	}

	public void stopScrapping() {
		updateAppState(AppState.IDLE);
	}

	public void onUpdateRandomness() {
		channelDetailsPanel.getRootState().updateRandomness(channelDetailsPanel.getRandomnessValueSlider().getValue());
	}

	private void updateAppState(AppState state) {
		channelDetailsPanel.getRootState().updateAppState(state);
	}
}
