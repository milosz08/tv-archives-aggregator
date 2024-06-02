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

package pl.miloszgilga.tvarchiver.webscrapper.file;

import pl.miloszgilga.tvarchiver.webscrapper.gui.AppIcon;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;

public class FileUtils {
	public static Optional<ImageIcon> getImageIconFromResources(Class<?> clazz, AppIcon appIcon) {
		final Optional<URL> iconUrl = getAssetFileFromResources(clazz, "assets/icons/%s.png", appIcon.getName());
		return iconUrl.map(ImageIcon::new);
	}

	public static Optional<URL> getAssetFileFromResources(Class<?> clazz, String resourcePath, Object... args) {
		final URL iconUrl = clazz.getResource(String.format("/%s", String.format(resourcePath, args)));
		return Optional.ofNullable(iconUrl);
	}
}
