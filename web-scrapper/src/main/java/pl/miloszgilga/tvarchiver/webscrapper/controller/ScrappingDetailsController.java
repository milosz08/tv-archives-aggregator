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
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ScrappingDetailsPanel;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

@Slf4j
@RequiredArgsConstructor
public class ScrappingDetailsController {
	private final ScrappingDetailsPanel scrappingDetailsPanel;

	public void onRowSelection(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) {
			return;
		}
		final RootState rootState = scrappingDetailsPanel.getRootState();
		final JTable detailsTable = scrappingDetailsPanel.getDetailsTable();
		final int selectedRowIndex = detailsTable.getSelectedRow();
		if (selectedRowIndex == -1) {
			return;
		}
		final int selectedYear = (Integer) detailsTable.getValueAt(selectedRowIndex, 0);
		rootState.updateSelectedYear(selectedYear);
		log.info("Narrow search content for: {} to year: {}", rootState.getSelectedChannel().name(), selectedYear);
	}
}
