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
import pl.miloszgilga.tvarchiver.webscrapper.controller.BottomBarController;
import pl.miloszgilga.tvarchiver.webscrapper.state.AppState;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomBarPanel extends JPanel {
	private final RootState rootState;
	private final BottomBarController controller;

	private final JLabel stateLabel;
	private final JLabel dbHostLabel;
	private final JLabel dbSizeLabel;
	private final JLabel memUsageLabel;

	public BottomBarPanel(RootState rootState) {
		this.rootState = rootState;
		controller = new BottomBarController(this);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		stateLabel = new JLabel(AppState.IDLE.createState());
		dbHostLabel = new JLabel(controller.parseToDbHost());
		dbSizeLabel = new JLabel(controller.parseToDbSize(0L));
		memUsageLabel = new JLabel(controller.getMemoryUsage());

		add(Box.createHorizontalGlue());
		addComponentWithBreadcrumb(stateLabel);
		addComponentWithBreadcrumb(dbHostLabel);
		addComponentWithBreadcrumb(dbSizeLabel);
		addComponentWithBreadcrumb(memUsageLabel);

		controller.updateMemoryUsage();
		controller.fetchDatabaseSize();
		controller.getJvmMeasurementsTimer().start();

		initObservables();
	}

	private void addComponentWithBreadcrumb(JLabel label) {
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(label);
		add(Box.createHorizontalStrut(20));
	}

	private void initObservables() {
		rootState.asDisposable(rootState.getAppState$(), appState -> {
			stateLabel.setText(appState.createState());
			final Timer dbSizeMeasurementsTimer = controller.getDbSizeMeasurementsTimer();
			if (appState.isIdle()) {
				dbSizeMeasurementsTimer.start();
			} else {
				dbSizeMeasurementsTimer.stop();
			}
		});
	}
}
