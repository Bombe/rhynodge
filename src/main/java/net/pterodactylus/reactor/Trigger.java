/*
 * Reactor - Trigger.java - Copyright © 2013 David Roden
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

import net.pterodactylus.reactor.states.FileState;

/**
 * A trigger determines whether two different states actually warrant a change
 * trigger. For example, two {@link FileState}s might contain different file
 * sizes but a trigger might only care about whether the file appeared or
 * disappeared since the last check.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Trigger {

	/**
	 * Checks whether the given states warrant a change trigger.
	 *
	 * @param currentState
	 *            The current state of a system
	 * @param previousState
	 *            The previous state of the system
	 * @return {@code true} if the given states warrant a change trigger,
	 *         {@code false} otherwise
	 */
	boolean triggers(State currentState, State previousState);

	/**
	 * Returns the object (or objects) that triggered a change. This method will
	 * only return a meaningful value when {@link #triggers(State, State)}
	 * returns {@code true}.
	 *
	 * @return The object that triggered a change
	 */
	Object trigger();

}
