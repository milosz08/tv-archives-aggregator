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

package pl.miloszgilga.tvarchiver.webscrapper.gui.window;

import lombok.Getter;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ChannelDetailsPanel;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ChannelsListPanel;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ConsolePanel;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.UnselectedChannelPanel;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class RootWindow extends AbstractWindow {
	private final RootState rootState;

	private final ChannelsListPanel channelsListPanel;
	private final JPanel rightCombinePanel;
	private final JPanel tvChannelContainerPanel;
	private final UnselectedChannelPanel unselectedChannelPanel;
	private final ChannelDetailsPanel channelDetailsPanel;
	private final ConsolePanel consolePanel;

	public RootWindow(RootState rootState) {
		super("TV Scrapper", 1280, 720, rootState);
		this.rootState = rootState;

		channelsListPanel = new ChannelsListPanel(rootState);
		rightCombinePanel = new JPanel();
		tvChannelContainerPanel = new JPanel();
		unselectedChannelPanel = new UnselectedChannelPanel();
		channelDetailsPanel = new ChannelDetailsPanel(rootState);
		consolePanel = new ConsolePanel();
	}

	@Override
	void appendElements(JFrame frame, JPanel rootPanel) {
		channelsListPanel.setPreferredSize(new Dimension(250, 720));
		unselectedChannelPanel.setPreferredSize(new Dimension(1030, 380));
		channelDetailsPanel.setPreferredSize(new Dimension(1030, 380));
		consolePanel.setPreferredSize(new Dimension(1030, 340));

		tvChannelContainerPanel.setLayout(new CardLayout());
		tvChannelContainerPanel.add(unselectedChannelPanel, "unselected");
		tvChannelContainerPanel.add(channelDetailsPanel, "selected");

		rightCombinePanel.setLayout(new BoxLayout(rightCombinePanel, BoxLayout.Y_AXIS));
		rightCombinePanel.add(tvChannelContainerPanel);
		rightCombinePanel.add(consolePanel);

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, channelsListPanel, rightCombinePanel);
		splitPane.setResizeWeight(0.25);
		splitPane.setDividerLocation(250);

		rootPanel.add(splitPane);
		initObservables();
	}

	private void initObservables() {
		rootState.wrapAsDisposable(rootState.getSelectedChannel$(), channel -> {
			final CardLayout cardLayout = (CardLayout) tvChannelContainerPanel.getLayout();
			cardLayout.show(tvChannelContainerPanel, channel.id() == 0 ? "unselected" : "selected");
		});
	}
}
