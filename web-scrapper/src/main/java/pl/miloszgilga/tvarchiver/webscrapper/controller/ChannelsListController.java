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
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.miloszgilga.tvarchiver.webscrapper.gui.MessageDialog;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ChannelsListPanel;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannelsSource;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChannelsListController {
	private final ChannelsListPanel channelsListPanel;

	public void fetchChannelsList() {
		final RootState rootState = channelsListPanel.getRootState();
		final JdbcTemplate jdbcTemplate = rootState.getJdbcTemplate();
		List<TvChannel> channels;
		if (jdbcTemplate == null) {
			return; // skipping, not initialized JDBC Template class
		}
		channelsListPanel.getChannelListModel().clear();
		rootState.updateTvChannels(new ArrayList<>());
		rootState.updateSelectedChannel(new TvChannel());
		// firstly, select data from persisted DB
		final List<TvChannel> fetchedTvChannels = jdbcTemplate.query("SELECT * FROM tv_channels", (rs, rowNum) ->
			new TvChannel(rs.getLong("id"), rs.getString("name"), rs.getString("slug"))
		);
		if (fetchedTvChannels.isEmpty()) {
			// if is empty, scrap data and insert into db
			final TvChannelsSource tvChannelsSource = new TvChannelsSource();
			final List<TvChannel> scrappedTvChannels = tvChannelsSource.getAllTvChannels();

			jdbcTemplate.batchUpdate("INSERT INTO tv_channels (name,slug) VALUES (?,?)",
				scrappedTvChannels, scrappedTvChannels.size(), (ps, arg) -> {
					ps.setString(1, arg.name());
					ps.setString(2, arg.slug());
				});
			log.info("Persisted DB data empty. Filling with {} fetched channels.", scrappedTvChannels.size());
			channels = scrappedTvChannels;
		} else {
			log.info("Found {} persisted channels.", fetchedTvChannels.size());
			channels = fetchedTvChannels;
		}
		rootState.updateTvChannels(channels);
		updateFetchedTvChannels(channels.size());
	}

	public void reFetchChannels() {
		fetchChannelsList();
		MessageDialog.showInfo("TV channels list was restored.");
	}

	public void removeSelection() {
		channelsListPanel.getChannels().clearSelection();
		channelsListPanel.getRootState().updateSelectedChannel(new TvChannel());
	}

	public void onListSelection(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			final RootState rootState = channelsListPanel.getRootState();
			final JList<TvChannel> tvChannels = channelsListPanel.getChannels();
			if (tvChannels.getSelectedIndex() != -1) {
				rootState.updateSelectedChannel(tvChannels.getSelectedValue());
			}
		}
	}

	public void performListFiltering() {
		final String searchTerm = channelsListPanel.getSearchField().getText()
			.toLowerCase()
			.trim();
		final RootState rootState = channelsListPanel.getRootState();
		final DefaultListModel<TvChannel> tvChannelsModel = channelsListPanel.getChannelListModel();

		tvChannelsModel.clear();
		for (final TvChannel tvChannel : rootState.getTvChannels()) {
			final String name = tvChannel.name().toLowerCase();
			if (searchTerm.isEmpty() || name.contains(searchTerm)) {
				tvChannelsModel.addElement(tvChannel);
			}
		}
		updateFetchedTvChannels(tvChannelsModel.size());
	}

	private void updateFetchedTvChannels(int size) {
		channelsListPanel.getFetchedChannelsLabel().setText(String.format("Found %d TV channels", size));
	}
}
