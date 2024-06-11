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
import pl.miloszgilga.tvarchiver.webscrapper.controller.ChannelDetailsController;
import pl.miloszgilga.tvarchiver.webscrapper.gui.FrameTaskbar;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.AbstractWindow;
import pl.miloszgilga.tvarchiver.webscrapper.state.ChannelDetailsTotalFetchedAggregator;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;
import pl.miloszgilga.tvarchiver.webscrapper.util.Constant;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Hashtable;

@Getter
public class ChannelDetailsPanel extends JPanel {
	private static final int SIZE = 20;
	private static final int STEP = 1;

	private final RootState rootState;
	private final JFrame rootWindow;
	private final ChannelDetailsController controller;
	private final JPanel controlPanel;
	private final ScrappingDetailsPanel scrappingDetailsPanel;

	private final JSlider randomnessValueSlider;
	private final Border sliderFrame, sliderMargin;
	private final JButton removeSelectedYearButton;
	private final JButton startScrappingButton;
	private final JButton stopScrappingButton;
	private final JPanel progressBarPanel;
	private final JProgressBar progressBar;

	public ChannelDetailsPanel(RootState rootState, AbstractWindow rootWindow) {
		this.rootState = rootState;
		this.rootWindow = rootWindow;
		controller = new ChannelDetailsController(this, rootWindow.getMessageDialog());

		controlPanel = new JPanel();
		scrappingDetailsPanel = new ScrappingDetailsPanel(rootState);

		randomnessValueSlider = new JSlider(JSlider.VERTICAL, rootState.getRandomness(), SIZE, 2);
		sliderFrame = BorderFactory.createTitledBorder("Randomness");
		sliderMargin = BorderFactory.createEmptyBorder(0, 20, 0, 20);
		removeSelectedYearButton = new JButton("Remove selection");
		startScrappingButton = new JButton("Start scrapping");
		stopScrappingButton = new JButton("Stop scrapping");
		progressBarPanel = new JPanel();
		progressBar = new JProgressBar();

		randomnessValueSlider.setBorder(BorderFactory.createCompoundBorder(sliderFrame, sliderMargin));
		randomnessValueSlider.setMinorTickSpacing(STEP);
		randomnessValueSlider.setMajorTickSpacing(STEP);
		randomnessValueSlider.setPaintTicks(true);
		randomnessValueSlider.setPaintLabels(true);
		randomnessValueSlider.setLabelTable(createSliderLabels());
		randomnessValueSlider.addChangeListener(e -> controller.onUpdateRandomness());

		stopScrappingButton.setEnabled(false);

		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setStringPainted(true);
		progressBar.setString(progressBar.getValue() + "%");

		removeSelectedYearButton.addActionListener(e -> controller.removeSelectedYear());
		startScrappingButton.addActionListener(e -> controller.startScrapping());
		stopScrappingButton.addActionListener(e -> controller.stopScrapping());

		progressBarPanel.setLayout(new GridLayout(1, 1));
		progressBarPanel.add(progressBar);

		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(removeSelectedYearButton);
		controlPanel.add(progressBarPanel);
		controlPanel.add(startScrappingButton);
		controlPanel.add(stopScrappingButton);

		setLayout(new BorderLayout());

		add(scrappingDetailsPanel, BorderLayout.CENTER);
		add(randomnessValueSlider, BorderLayout.EAST);
		add(controlPanel, BorderLayout.SOUTH);

		initObservables();
	}

	private Hashtable<Integer, JLabel> createSliderLabels() {
		final Hashtable<Integer, JLabel> labelsTable = new Hashtable<>();
		for (int i = 0; i <= SIZE; i += STEP) {
			if (i != 0) {
				labelsTable.put(i, new JLabel(i + "s"));
			}
		}
		return labelsTable;
	}

	private void updateProgress(double percentage) {
		SwingUtilities.invokeLater(() -> {
			progressBar.setValue((int) percentage);
			progressBar.setString(Constant.PF.format(percentage) + "%");
		});
	}

	private void initObservables() {
		rootState.asDisposable(rootState.getSelectedChannel$(), controller::onSwitchChannel);
		rootState.asDisposable(rootState.getSelectedYear$(), y -> removeSelectedYearButton.setEnabled(!y.isEmpty()));
		rootState.asDisposable(rootState.getProgressBar$(), percentage -> {
			updateProgress(percentage);
			FrameTaskbar.setProgress(rootWindow, percentage);
		});
		rootState.asDisposable(rootState.getAppState$(), state -> {
			randomnessValueSlider.setEnabled(state.isIdle());
			startScrappingButton.setEnabled(state.isIdle());
			stopScrappingButton.setEnabled(!state.isIdle());
		});
	}
}
