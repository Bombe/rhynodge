/*
 * Reactor - FileStateModifiedTrigger.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Preconditions.checkState;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.FileState;

/**
 * {@link Trigger} that checks for modifications of a file using the existence,
 * size, and modification time of the {@link FileState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileStateModifiedTrigger implements Trigger {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(State currentState, State previousState) {
		checkState(currentState instanceof FileState, "currentState is not a FileState but a %s", currentState.getClass());
		checkState(previousState instanceof FileState, "previousState is not a FileState but a %s", currentState.getClass());
		FileState currentFileState = (FileState) currentState;
		FileState previousFileState = (FileState) previousState;
		return (currentFileState.exists() != previousFileState.exists()) || (currentFileState.size() != previousFileState.size()) || (currentFileState.modificationTime() != previousFileState.modificationTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object trigger() {
		return null;
	}

}
