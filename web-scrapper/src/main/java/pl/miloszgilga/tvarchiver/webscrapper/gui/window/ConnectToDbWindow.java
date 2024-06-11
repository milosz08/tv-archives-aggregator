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
import pl.miloszgilga.tvarchiver.webscrapper.controller.ConnectToDbController;
import pl.miloszgilga.tvarchiver.webscrapper.state.EnvKey;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class ConnectToDbWindow extends AbstractWindow {
	private final RootState rootState;
	private final ConnectToDbController controller;

	private final JPanel inputFieldsPanel;

	private final JLabel hostLabel;
	private final JTextField hostField;
	private final JLabel portLabel;
	private final JTextField portField;
	private final JLabel usernameLabel;
	private final JTextField usernameField;
	private final JLabel passwordLabel;
	private final JPasswordField passwordField;
	private final JLabel dbNameLabel;
	private final JTextField dbNameField;

	private final JButton confirmButton;

	public ConnectToDbWindow(RootState rootState) {
		super("Connect to DB", 300, 150, rootState);
		this.rootState = rootState;
		controller = new ConnectToDbController(this, getMessageDialog());

		inputFieldsPanel = new JPanel();

		hostLabel = new JLabel("Host");
		hostField = new JTextField(rootState.getEnvValue(EnvKey.DB_HOST));
		portLabel = new JLabel("Port");
		portField = new JTextField(rootState.getEnvValue(EnvKey.DB_PORT));
		usernameLabel = new JLabel("Username");
		usernameField = new JTextField(rootState.getEnvValue(EnvKey.DB_USERNAME));
		passwordLabel = new JLabel("Password");
		passwordField = new JPasswordField(rootState.getEnvValue(EnvKey.DB_PASSWORD));
		dbNameLabel = new JLabel("DB name");
		dbNameField = new JPasswordField(rootState.getEnvValue(EnvKey.DB_NAME));

		confirmButton = new JButton("Connect to DB");
		confirmButton.addActionListener(e -> controller.onClickConnectToDb());
	}

	@Override
	void appendElements(JFrame frame, JPanel rootPanel) {
		rootPanel.setLayout(new BorderLayout());
		inputFieldsPanel.setLayout(new GridLayout(4, 2));

		inputFieldsPanel.add(hostLabel);
		inputFieldsPanel.add(hostField);
		inputFieldsPanel.add(portLabel);
		inputFieldsPanel.add(portField);
		inputFieldsPanel.add(usernameLabel);
		inputFieldsPanel.add(usernameField);
		inputFieldsPanel.add(passwordLabel);
		inputFieldsPanel.add(passwordField);

		rootPanel.add(inputFieldsPanel, BorderLayout.CENTER);
		rootPanel.add(confirmButton, BorderLayout.SOUTH);
	}
}
