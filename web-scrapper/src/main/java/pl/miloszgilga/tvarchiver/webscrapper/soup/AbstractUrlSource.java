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

package pl.miloszgilga.tvarchiver.webscrapper.soup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.miloszgilga.tvarchiver.webscrapper.gui.InoperableException;

import java.io.IOException;

@Slf4j
abstract class AbstractUrlSource {
	protected Document rootNode;

	public AbstractUrlSource(UrlSource urlSource) {
		try {
			rootNode = Jsoup.connect(urlSource.getUrl())
				.userAgent("Mozilla")
				.get();
			log.debug("Successfully connected to: {}", urlSource);
		} catch (IOException ex) {
			throw new InoperableException(ex);
		}
	}
}
