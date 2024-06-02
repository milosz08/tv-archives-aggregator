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

package pl.miloszgilga.tvarchiver.webscrapper.gui;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

@Slf4j
public class GuiThread implements Runnable {
	private final LookAndFeel defaultLookAndFeel;
	private final RootState rootState;

	public GuiThread() {
		defaultLookAndFeel = new MetalLookAndFeel();
		rootState = new RootState();
	}

	public void initAndStartThread() {
		try {
			UIManager.setLookAndFeel(defaultLookAndFeel);
			SwingUtilities.invokeLater(this);
		} catch (UnsupportedLookAndFeelException ex) {
			log.error(ex.getMessage());
		}
	}

	@Override
	public void run() {
		log.info("Starting GUI thread");
		try {
			final RootWindow rootWindow = new RootWindow(rootState);
			rootWindow.initAndShow();
			log.info("Initialized application GUI.");
		} catch (InoperableException ex) {
			log.error(ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
