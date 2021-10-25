/*
 * Rhynodge - Output.java - Copyright © 2013 David Roden
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

import net.pterodactylus.rhynodge.State;

/**
 * Defines the output of a {@link State}. As different output has to be
 * generated for different media, the {@link #text(String)} method takes as
 * an argument the MIME type of the desired output.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Output {

	/**
	 * Returns a short summary that can be included e. g. in the subject of an
	 * email.
	 *
	 * @return A short summary of the output
	 */
	String summary();

	/**
	 * Returns the text for the given MIME type and the given maximum length.
	 * Note that the maximum length does not need to be enforced at all costs;
	 * implementation are free to return texts longer than the given number of
	 * characters.
	 *
	 * @param mimeType
	 *            The MIME type of the text (“text/plain” and “text/html” should
	 *            be supported by all {@link State}s)
	 * @return The text for the given MIME type, or {@code null} if there is no
	 *         text defined for the given MIME type
	 */
	String text(String mimeType);

}
