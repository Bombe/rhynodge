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

import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.FileState;

/**
 * A trigger that detects changes in the existence of a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileExistenceTrigger implements Trigger<FileState> {

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(FileState previousState, FileState currentState) {
		return previousState.exists() != currentState.exists();
	}

}
