/*
 * Rhynodge - Filter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge;

/**
 * Defines a filter that runs between {@link Query}s and {@link Trigger}s and
 * can be used to convert a {@link State} into another {@link State}. This can
 * be used to extract further information from a state.
 * <p>
 * An example scenario would be a {@link Query} that requests a web site and a
 * {@link Filter} that extracts content from the web site. That way the same
 * {@link Query} could be used for multiple {@link Reaction}s without requiring
 * modifications.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Filter {

	/**
	 * Converts the given state into a different state.
	 *
	 * @param state
	 *            The state to convert
	 * @return The new state
	 */
	State filter(State state);

}
