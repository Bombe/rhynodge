/*
 * Rhynodge - DefaultOutput.java - Copyright © 2013 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.rhynodge.output;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * {@link Output} implementation that stores texts for arbitrary MIME types.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultOutput implements Output {

	/** The summary of the output. */
	private final String summary;

	/** The texts for the different MIME types. */
	private final Map<String, String> mimeTypeTexts = Maps.newHashMap();

	/**
	 * Creates a new default output.
	 *
	 * @param summary
	 *            The summary of the output
	 */
	public DefaultOutput(String summary) {
		this.summary = summary;
	}

	//
	// ACTIONS
	//

	/**
	 * Adds the given text for the given MIME type.
	 *
	 * @param mimeType
	 *            The MIME type to add the text for
	 * @param text
	 *            The text to add
	 * @return This default output
	 */
	public DefaultOutput addText(String mimeType, String text) {
		mimeTypeTexts.put(mimeType, text);
		return this;
	}

	//
	// OUTPUT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String summary() {
		return summary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String text(String mimeType, int maxLength) {
		return mimeTypeTexts.get(mimeType);
	}

}
