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
import pl.miloszgilga.tvarchiver.webscrapper.db.DataSource;
import pl.miloszgilga.tvarchiver.webscrapper.gui.MessageDialog;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.ConnectToDbWindow;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.RootWindow;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import java.net.ConnectException;

@RequiredArgsConstructor
public class ConnectToDbController {
	private final ConnectToDbWindow connectToDbWindow;

	public void onClickConnectToDb() {
		final String host = connectToDbWindow.getHostField().getText();
		final String port = connectToDbWindow.getPortField().getText();
		final String username = connectToDbWindow.getUsernameField().getText();
		final String password = new String(connectToDbWindow.getPasswordField().getPassword());
		final String dbName = connectToDbWindow.getDbNameField().getText();
		try {
			final int parsedPort = Integer.parseInt(port);
			final RootState rootState = connectToDbWindow.getRootState();
			final DataSource dataSource = new DataSource(host, parsedPort, username, password, dbName);
			if (!dataSource.isSuccessfullyConnected()) {
				throw new ConnectException();
			}
			rootState.setDataSource(dataSource);
			final RootWindow rootWindow = new RootWindow(rootState);
			rootWindow.createWindow();
			connectToDbWindow.closeWindow();
		} catch (NumberFormatException ex) {
			MessageDialog.showError("Incorrect port");
		} catch (ConnectException ex) {
			MessageDialog.showError("Unable to connect to DB");
		}
	}
}
