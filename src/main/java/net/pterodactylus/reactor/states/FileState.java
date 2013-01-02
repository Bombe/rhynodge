/*
 * Reactor - FileState.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.states;

import net.pterodactylus.reactor.State;

/**
 * A {@link State} that contains information about a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileState extends AbstractState {

	/** Whether the file exists. */
	private final boolean exists;

	/** Whether the file is readable. */
	private final boolean readable;

	/** The size of the file. */
	private final long size;

	/** The modification time of the file. */
	private final long modificationTime;

	/**
	 * Creates a new file state that signals that an exceptio occured during
	 * retrieval.
	 *
	 * @param exception
	 *            The exception that occured
	 */
	public FileState(Throwable exception) {
		super(exception);
		exists = false;
		readable = false;
		size = -1;
		modificationTime = -1;
	}

	/**
	 * Creates a new file state.
	 *
	 * @param exists
	 *            {@code true} if the file exists, {@code false} otherwise
	 * @param readable
	 *            {@code true} if the file is readable, {@code false} otherwise
	 * @param size
	 *            The size of the file (in bytes)
	 * @param modificationTime
	 *            The modification time of the file (in milliseconds since Jan
	 *            1, 1970 UTC)
	 */
	public FileState(boolean exists, boolean readable, long size, long modificationTime) {
		this.exists = exists;
		this.readable = readable;
		this.size = size;
		this.modificationTime = modificationTime;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns whether the file exists.
	 *
	 * @return {@code true} if the file exists, {@code false} otherwise
	 */
	public boolean exists() {
		return exists;
	}

	/**
	 * Returns whether the file is readable.
	 *
	 * @return {@code true} if the file is readable, {@code false} otherwise
	 */
	public boolean readable() {
		return readable;
	}

	/**
	 * Returns the size of the file.
	 *
	 * @return The size of the file (in bytes)
	 */
	public long size() {
		return size;
	}

	/**
	 * Returns the modification time of the file.
	 *
	 * @return The modification time of the file (in milliseconds since Jan 1,
	 *         1970 UTC)
	 */
	public long modificationTime() {
		return modificationTime;
	}

}
