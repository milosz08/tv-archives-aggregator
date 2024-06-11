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

package pl.miloszgilga.tvarchiver.webscrapper.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ConsolePanel;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class LogbackTextAreaAppender extends AppenderBase<ILoggingEvent> {
	private static final int LIMIT = 1000;
	private static final int CUT_RANGE = 200;
	private PatternLayout patternLayout;

	@Override
	public void start() {
		patternLayout = new PatternLayout();
		patternLayout.setContext(getContext());
		patternLayout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} > [%-5p] : %m%n");
		patternLayout.start();
		super.start();
	}

	@Override
	protected void append(ILoggingEvent iLoggingEvent) {
		SwingUtilities.invokeLater(() -> {
			final String formattedMsg = patternLayout.doLayout(iLoggingEvent);
			final JTextArea textArea = ConsolePanel.textArea;
			if (textArea == null) {
				return;
			}
			try {
				final Document document = textArea.getDocument();
				if (document.getDefaultRootElement().getElementCount() > LIMIT) {
					replaceRange(textArea, getLineEndOffset(textArea));
				}
				textArea.append(formattedMsg);
			} catch (RuntimeException ignored) {
			}
			textArea.setCaretPosition(textArea.getDocument().getLength());
		});
	}

	private void replaceRange(JTextArea textArea, int end) {
		final Document doc = textArea.getDocument();
		if (doc == null) {
			return;
		}
		try {
			if (doc instanceof AbstractDocument) {
				((AbstractDocument) doc).replace(0, end, null, null);
			} else {
				doc.remove(0, end);
				doc.insertString(0, null, null);
			}
		} catch (BadLocationException e) {
			throw new RuntimeException();
		}
	}

	private int getLineEndOffset(JTextArea textArea) {
		int lineCount = textArea.getDocument().getDefaultRootElement().getElementCount();
		if (CUT_RANGE >= lineCount) {
			throw new RuntimeException();
		}
		final Element map = textArea.getDocument().getDefaultRootElement();
		final Element lineElem = map.getElement(CUT_RANGE);
		final int endOffset = lineElem.getEndOffset();
		return (CUT_RANGE == lineCount - 1) ? (endOffset - 1) : endOffset;
	}
}
