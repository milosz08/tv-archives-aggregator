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
import pl.miloszgilga.tvarchiver.webscrapper.controller.ConsolePanelController;
import pl.miloszgilga.tvarchiver.webscrapper.gui.AppIcon;
import pl.miloszgilga.tvarchiver.webscrapper.gui.component.JAppIconButton;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;

@Getter
public class ConsolePanel extends JPanel {
	public static JTextArea textArea;

	private final ConsolePanelController controller;
	private final JScrollPane scrollPane;
	private final JToolBar rightButtonsToolbar;

	private final JAppIconButton moveToUpButton;
	private final JAppIconButton moveToDownButton;
	private final JAppIconButton clearButton;
	private final JAppIconButton printToFileButton;

	public ConsolePanel(AbstractWindow rootWindow) {
		controller = new ConsolePanelController(this, rootWindow.getMessageDialog());

		setLayout(new BorderLayout());

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		rightButtonsToolbar = new JToolBar();

		textArea.setEditable(false);
		textArea.setFont(new Font("monospaced", textArea.getFont().getStyle(), textArea.getFont().getSize()));

		rightButtonsToolbar.setLayout(new BoxLayout(rightButtonsToolbar, BoxLayout.Y_AXIS));
		rightButtonsToolbar.setFloatable(false);

		moveToUpButton = new JAppIconButton("Move to up", AppIcon.UPLOAD, true);
		moveToDownButton = new JAppIconButton("Move to down", AppIcon.DOWNLOAD, true);
		clearButton = new JAppIconButton("Clear", AppIcon.DELETE, true);
		printToFileButton = new JAppIconButton("Print to file", AppIcon.PRINT, true);

		moveToUpButton.addActionListener(e -> controller.moveUp());
		moveToDownButton.addActionListener(e -> controller.moveDown());
		clearButton.addActionListener(e -> controller.clearText());
		printToFileButton.addActionListener(e -> controller.printToFile());

		rightButtonsToolbar.add(moveToUpButton);
		rightButtonsToolbar.add(moveToDownButton);
		rightButtonsToolbar.add(clearButton);
		rightButtonsToolbar.add(printToFileButton);

		add(scrollPane, BorderLayout.CENTER);
		add(rightButtonsToolbar, BorderLayout.EAST);
	}
}
