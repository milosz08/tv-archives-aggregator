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

import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

public class RootWindow extends JFrame {
	private static final int WIDTH = 520;
	private static final int HEIGHT = 400;
	private static final String NAME = "TV Web Scrapper";

	private final RootState rootState;
	private final Dimension size;
	private final GuiWindowAdapter guiWindowAdapter;

	public RootWindow(RootState rootState) {
		this.rootState = rootState;
		size = new Dimension(WIDTH, HEIGHT);
		guiWindowAdapter = new GuiWindowAdapter(this, rootState);
	}

	public void initAndShow() {
		setSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(guiWindowAdapter);
		setTitle(NAME);
		setLayout(new BorderLayout());
		extendsFrame();
		setVisible(true);
	}

	private void extendsFrame() {
	}
}
