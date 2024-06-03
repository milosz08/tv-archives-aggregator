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
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Hashtable;

@Getter
public class ChannelDetailsPanel extends JPanel {
	private static final int SIZE = 20;
	private static final int STEP = 1;

	private final RootState rootState;
	private final ChannelDetailsController controller;

	private final JPanel controlPanel;

	private final JLabel label;
	private final JSlider randomnessValueSlider;
	private final Border sliderFrame, sliderMargin;
	private final JButton startScrappingButton;
	private final JButton stopScrappingButton;
	private final JPanel progressBarPanel;
	private final JProgressBar progressBar;

	public ChannelDetailsPanel(RootState rootState) {
		this.rootState = rootState;
		controller = new ChannelDetailsController(this);

		controlPanel = new JPanel();

		label = new JLabel();
		randomnessValueSlider = new JSlider(JSlider.VERTICAL, 1, SIZE, 2);
		sliderFrame = BorderFactory.createTitledBorder("Randomness");
		sliderMargin = BorderFactory.createEmptyBorder(0, 20, 0, 20);
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

		startScrappingButton.addActionListener(e -> controller.startScrapping());
		stopScrappingButton.addActionListener(e -> controller.stopScrapping());

		progressBarPanel.setLayout(new GridLayout(1, 1));
		progressBarPanel.add(progressBar);

		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(startScrappingButton);
		controlPanel.add(stopScrappingButton);
		controlPanel.add(progressBarPanel);

		setLayout(new BorderLayout());

		add(label, BorderLayout.CENTER);
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

	private void updateProgress(int percentage) {
		SwingUtilities.invokeLater(() -> {
			progressBar.setValue(percentage);
			progressBar.setString(percentage + "%");
		});
	}

	private void initObservables() {
		rootState.asDisposable(rootState.getSelectedChannel$(), channel -> label.setText(channel.name()));
		rootState.asDisposable(rootState.getProgressBar$(), this::updateProgress);
		rootState.asDisposable(rootState.getAppState$(), state -> {
			randomnessValueSlider.setEnabled(state.isIdle());
			startScrappingButton.setEnabled(state.isIdle());
			stopScrappingButton.setEnabled(!state.isIdle());
		});
	}
}
