/*
 * Reactor - FileExistenceTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.triggers;

import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.FileState;

import com.google.common.base.Preconditions;

/**
 * A trigger that detects changes in the existence of a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileExistenceTrigger implements Trigger {

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(State previousState, State currentState) {
		Preconditions.checkState(previousState instanceof FileState, "previousState is not a FileState");
		Preconditions.checkState(currentState instanceof FileState, "currentState is not a FileState");
		return ((FileState) previousState).exists() != ((FileState) currentState).exists();
	}

}
