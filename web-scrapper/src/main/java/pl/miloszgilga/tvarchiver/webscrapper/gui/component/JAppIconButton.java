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

package pl.miloszgilga.tvarchiver.webscrapper.gui.component;

import pl.miloszgilga.tvarchiver.webscrapper.file.FileUtils;
import pl.miloszgilga.tvarchiver.webscrapper.gui.AppIcon;

import javax.swing.*;

public class JAppIconButton extends JButton {
	public JAppIconButton(String text, AppIcon iconName, boolean setDescription) {
		if (setDescription) {
			setToolTipText(text);
		} else {
			setText(text);
		}
		FileUtils.getImageIconFromResources(getClass(), iconName).ifPresent(this::setIcon);
	}
}