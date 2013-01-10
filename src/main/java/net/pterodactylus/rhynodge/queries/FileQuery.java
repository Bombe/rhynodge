/*
 * Rhynodge - FileQuery.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.queries;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FileState;

/**
 * Queries the filesystem about a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileQuery implements Query {

	/** The name of the file to query. */
	private final String filename;

	/**
	 * Creates a new file query.
	 *
	 * @param filename
	 *            The name of the file to query
	 */
	public FileQuery(String filename) {
		this.filename = checkNotNull(filename, "filename must not be null");
	}

	//
	// QUERY METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State state() {
		File file = new File(filename);
		if (!file.exists()) {
			return new FileState(false, false, -1, -1);
		}
		if (!file.canRead()) {
			return new FileState(true, false, -1, -1);
		}
		return new FileState(true, true, file.length(), file.lastModified());
	}

}
