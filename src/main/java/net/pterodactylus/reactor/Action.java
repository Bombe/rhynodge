/*
 * Reactor - Action.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor;

/**
 * An action is performed when a {@link Trigger} determines that two given
 * {@link State}s of a {@link Query} signify a change.
 *
 * @param <S>
 *            The type of the state
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Action<S extends State> {

	/**
	 * Performs the action.
	 *
	 * @param currentState
	 *            The current state of a system
	 * @param previousState
	 *            The previous state of the system
	 */
	void execute(S currentState, S previousState);

}
