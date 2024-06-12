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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
public class GuiWindowAdapter extends WindowAdapter {
	private final MessageDialog messageDialog;
	private final RootState rootState;

	private boolean isScrapping;

	public GuiWindowAdapter(MessageDialog messageDialog, RootState rootState) {
		this.messageDialog = messageDialog;
		this.rootState = rootState;
		initObservables();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (isScrapping) {
			messageDialog.showError("You cannot close the window while scrapping is active!");
			return;
		}
		final int result = messageDialog.showConfirm("Are you sure you want to exit?");
		if (result == JOptionPane.YES_OPTION) {
			rootState.cleanupAndDisposableSubscription();
			System.exit(0);
		}
	}

	private void initObservables() {
		rootState.asDisposable(rootState.getAppState$(), state -> isScrapping = !state.isIdle());
	}
}
