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

package pl.miloszgilga.tvarchiver.webscrapper.gui.panel;

import lombok.Getter;
import pl.miloszgilga.tvarchiver.webscrapper.controller.ChannelsListController;
import pl.miloszgilga.tvarchiver.webscrapper.gui.filter.TvChannelFilterListener;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.AbstractWindow;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class ChannelsListPanel extends JPanel {
	private final RootState rootState;
	private final ChannelsListController controller;

	private final JPanel channelsPanel;

	private final JTextField searchField;
	private final DefaultListModel<TvChannel> channelListModel;
	private final JList<TvChannel> channels;
	private final JScrollPane scrollPane;

	private final JPanel buttonWithTextPanel;
	private final JButton reFetchChannelsButton;
	private final JButton removeSelectionButton;
	private final JLabel fetchedChannelsLabel;

	public ChannelsListPanel(RootState rootState, AbstractWindow rootWindow) {
		this.rootState = rootState;
		controller = new ChannelsListController(this, rootWindow.getMessageDialog());

		channelsPanel = new JPanel();
		searchField = new JTextField();

		channelListModel = new DefaultListModel<>();
		channels = new JList<>(channelListModel);
		channels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		channels.addListSelectionListener(controller::onListSelection);
		scrollPane = new JScrollPane(channels);

		buttonWithTextPanel = new JPanel();
		reFetchChannelsButton = new JButton("Re-fetch TV channels");
		removeSelectionButton = new JButton("Remove selection");
		fetchedChannelsLabel = new JLabel("Found 0 TV channels");

		reFetchChannelsButton.addActionListener(e -> controller.reFetchChannels());
		removeSelectionButton.addActionListener(e -> controller.removeSelection());

		buttonWithTextPanel.setLayout(new BorderLayout());
		buttonWithTextPanel.add(reFetchChannelsButton, BorderLayout.NORTH);
		buttonWithTextPanel.add(removeSelectionButton, BorderLayout.CENTER);
		buttonWithTextPanel.add(fetchedChannelsLabel, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(searchField, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonWithTextPanel, BorderLayout.SOUTH);

		initObservables();

		controller.fetchChannelsList();
		searchField.getDocument().addDocumentListener(new TvChannelFilterListener(controller));
	}

	private void initObservables() {
		rootState.asDisposable(rootState.getTvChannels$(), channelListModel::addAll);
		rootState.asDisposable(rootState.getAppState$(), state -> {
			reFetchChannelsButton.setEnabled(state.isIdle());
			removeSelectionButton.setEnabled(state.isIdle());
			channels.setEnabled(state.isIdle());
		});
	}
}
