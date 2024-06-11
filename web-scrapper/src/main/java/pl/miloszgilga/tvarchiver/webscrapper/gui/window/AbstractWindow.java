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
import pl.miloszgilga.tvarchiver.webscrapper.gui.GuiWindowAdapter;
import pl.miloszgilga.tvarchiver.webscrapper.gui.MessageDialog;
import pl.miloszgilga.tvarchiver.webscrapper.state.AbstractDisposableProvider;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {
	private final String title;
	private final Dimension size;
	private final GuiWindowAdapter guiWindowAdapter;
	private final JPanel rootPanel;
	@Getter
	private final MessageDialog messageDialog;

	public AbstractWindow(String title, int width, int height, AbstractDisposableProvider disposableProvider) {
		this.title = title;
		rootPanel = new JPanel();
		size = new Dimension(width, height);
		messageDialog = new MessageDialog(this);
		guiWindowAdapter = new GuiWindowAdapter(this, messageDialog, disposableProvider);
	}

	public void createWindow() {
		rootPanel.setBounds(0, 0, getWidth(), getHeight());
		setSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setLocation(getMotherScreenCenter());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(guiWindowAdapter);
		setTitle(title);
		setLayout(new BorderLayout());
		add(rootPanel, BorderLayout.CENTER);
		appendElements(this, rootPanel);
		setVisible(true);
		pack();
	}

	public void closeWindow() {
		setVisible(false);
		dispose();
	}

	private Point getMotherScreenCenter() {
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = dimension.width / 2 - getWidth() / 2;
		final int y = dimension.height / 2 - getHeight() / 2;
		return new Point(x, y);
	}

	abstract void appendElements(JFrame frame, JPanel rootPanel);
}
