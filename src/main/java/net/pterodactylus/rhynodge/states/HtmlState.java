/*
 * Rhynodge - HtmlState.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.states;

import net.pterodactylus.rhynodge.State;

import org.jsoup.nodes.Document;

/**
 * {@link State} implementation that contains a parsed HTML {@link Document}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HtmlState extends AbstractState {

	/** The URI of the parsed document. */
	private final String uri;

	/** The parsed document. */
	private final Document document;

	/**
	 * Creates a new HTML state.
	 *
	 * @param uri
	 *            The URI of the parsed document
	 * @param document
	 *            The parsed documnet
	 */
	public HtmlState(String uri, Document document) {
		this.uri = uri;
		this.document = document;
	}

	//
	// ACCESSORS
	//

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns the URI of the parsed document.
	 *
	 * @return The URI of the parsed document
	 */
	public String uri() {
		return uri;
	}

	/**
	 * Returns the parsed document.
	 *
	 * @return The parsed document
	 */
	public Document document() {
		return document;
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[document=(%s chars)]", getClass().getSimpleName(), document().toString().length());
	}

}
