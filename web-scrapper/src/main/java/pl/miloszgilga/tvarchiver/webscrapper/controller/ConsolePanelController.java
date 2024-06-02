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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import pl.miloszgilga.tvarchiver.webscrapper.gui.MessageDialog;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ConsolePanel;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class ConsolePanelController {
	private final ConsolePanel consolePanel;

	public void moveUp() {
		final JScrollBar verticalScrollBar = consolePanel.getScrollPane().getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMinimum());
	}

	public void moveDown() {
		final JScrollBar verticalScrollBar = consolePanel.getScrollPane().getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
	}

	public void clearText() {
		final int result = JOptionPane.showConfirmDialog(consolePanel, "Are you sure to clear console content?",
			"Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.YES_OPTION) {
			final JTextArea textArea = ConsolePanel.textArea;
			textArea.setText(StringUtils.EMPTY);
		}
	}

	public void printToFile() {
		final FastDateFormat targetFormat = FastDateFormat.getInstance("MM-dd-yyyy-HH-mm-ss-SSS");
		final String fileName = String.format("%s-%s", targetFormat.format(new Date()), "log.txt");

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(fileName));

		final int result = fileChooser.showSaveDialog(null);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		final File file = fileChooser.getSelectedFile();
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(ConsolePanel.textArea.getText());
			MessageDialog.showInfo("Logs saved to: " + file.getAbsolutePath());
		} catch (IOException ex) {
			MessageDialog.showError("An error occurred while saving logs to file!");
		}
	}
}
