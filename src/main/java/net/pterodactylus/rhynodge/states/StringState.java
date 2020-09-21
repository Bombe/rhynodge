/*
 * rhynodge - StringState.java - Copyright © 2013 David Roden
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

import javax.annotation.Nonnull;

/**
 * A {@link net.pterodactylus.rhynodge.State} that stores a single {@link
 * String} value.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StringState extends AbstractState {

	/** The string value. */
	private final String value;

	/**
	 * Creates a new string state.
	 *
	 * @param value
	 * 		The value of the state
	 */
	public StringState(String value) {
		this.value = value;
	}

	/**
	 * Returns the string value of this state.
	 *
	 * @return The string value
	 */
	public String value() {
		return value;
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Nonnull
	@Override
	protected String plainText() {
		return value;
	}

}
