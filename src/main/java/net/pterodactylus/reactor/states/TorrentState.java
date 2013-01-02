/*
 * Reactor - TorrentState.java - Copyright © 2013 David Roden
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

import java.util.Iterator;
import java.util.List;

import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

import com.google.common.collect.Lists;

/**
 * {@link State} that contains information about an arbitrary number of torrent
 * files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentState extends AbstractState implements Iterable<TorrentFile> {

	/** The torrent files. */
	private final List<TorrentFile> files = Lists.newArrayList();

	//
	// ACCESSORS
	//

	/**
	 * Adds a torrent file to this state.
	 *
	 * @param torrentFile
	 *            The torrent file to add
	 * @return This state
	 */
	public TorrentState addTorrentFile(TorrentFile torrentFile) {
		files.add(torrentFile);
		return this;
	}

	//
	// ITERABLE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<TorrentFile> iterator() {
		return files.iterator();
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[files=%s]", getClass().getSimpleName(), files);
	}

	/**
	 * Container for torrent file data.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class TorrentFile {

		/** The name of the file. */
		private final String name;

		/** The size of the file. */
		private final String size;

		/** The magnet URI of the file. */
		private final String magnetUri;

		/** The download URI of the file. */
		private final String downloadUri;

		/**
		 * Creates a new torrent file.
		 *
		 * @param name
		 *            The name of the file
		 * @param size
		 *            The size of the file
		 * @param magnetUri
		 *            The magnet URI of the file
		 * @param downloadUri
		 *            The download URI of the file
		 */
		public TorrentFile(String name, String size, String magnetUri, String downloadUri) {
			this.name = name;
			this.size = size;
			this.magnetUri = magnetUri;
			this.downloadUri = downloadUri;
		}

		//
		// ACCESSORS
		//

		/**
		 * Returns the name of the file.
		 *
		 * @return The name of the file
		 */
		public String name() {
			return name;
		}

		/**
		 * Returns the size of the file. The returned size may included
		 * non-numeric information, such as units (e. g. “860.46 MB”).
		 *
		 * @return The size of the file
		 */
		public String size() {
			return size;
		}

		/**
		 * Returns the magnet URI of the file.
		 *
		 * @return The magnet URI of the file
		 */
		public String magnetUri() {
			return magnetUri;
		}

		/**
		 * Returns the download URI of the file.
		 *
		 * @return The download URI of the file
		 */
		public String downloadUri() {
			return downloadUri;
		}

		//
		// OBJECT METHODS
		//

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return String.format("%s(%s,%s,%s)", name(), size(), magnetUri(), downloadUri());
		}

	}

}
