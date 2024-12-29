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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import pl.miloszgilga.tvarchiver.webscrapper.db.DataHandler;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.BottomBarPanel;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class BottomBarController {
	private final BottomBarPanel bottomBarPanel;
	@Getter
	private final Timer jvmMeasurementsTimer, dbSizeMeasurementsTimer;

	public BottomBarController(BottomBarPanel bottomBarPanel) {
		this.bottomBarPanel = bottomBarPanel;
		jvmMeasurementsTimer = new Timer(5000, e -> updateMemoryUsage());
		dbSizeMeasurementsTimer = new Timer(10000, e -> fetchDatabaseSize());
	}

	public String getMemoryUsage() {
		return parseBytes("Memory", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	public String parseToDbSize(long bytes) {
		return parseBytes("DB size", bytes);
	}

	public String parseToDbHost() {
		final InetSocketAddress address = bottomBarPanel.getRootState().getDataHandler().getDbHost();
		return String.format("DB: %s:%s", address.getHostString(), address.getPort());
	}

	public void updateMemoryUsage() {
		bottomBarPanel.getMemUsageLabel().setText(getMemoryUsage());
	}

	public void fetchDatabaseSize() {
		final RootState rootState = bottomBarPanel.getRootState();
		// fetch only when is running and after took first size
		final DataHandler dataHandler = rootState.getDataHandler();
		final Long dbSize = dataHandler.getDatabaseSize();
		if (dbSize != null) {
			bottomBarPanel.getDbSizeLabel().setText(parseToDbSize(dbSize));
		}
	}

	private String parseBytes(String prefix, long bytes) {
		String formattedBytes;
		try {
			formattedBytes = FileUtils.byteCountToDisplaySize(bytes);
		} catch (IllegalArgumentException ex) {
			formattedBytes = "0MB";
		}
		return String.format("%s: %s", prefix, formattedBytes);
	}
}
