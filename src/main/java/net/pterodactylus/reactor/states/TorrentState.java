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

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

/**
 * {@link State} that contains information about an arbitrary number of torrent
 * files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentState extends AbstractState implements Iterable<TorrentFile> {

	/** The torrent files. */
	@JsonProperty
	private List<TorrentFile> files = Lists.newArrayList();

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
		@JsonProperty
		private final String name;

		/** The size of the file. */
		@JsonProperty
		private final String size;

		/** The magnet URI of the file. */
		@JsonProperty
		private final String magnetUri;

		/** The download URI of the file. */
		@JsonProperty
		private final String downloadUri;

		/** The number of files in this torrent. */
		@JsonProperty
		private final int fileCount;

		/** The number of seeds connected to this torrent. */
		@JsonProperty
		private final int seedCount;

		/** The number of leechers connected to this torrent. */
		@JsonProperty
		private final int leechCount;

		/**
		 * No-arg constructor for deserialization.
		 */
		@SuppressWarnings("unused")
		private TorrentFile() {
			this(null, null, null, null, 0, 0, 0);
		}

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
		 * @param fileCount
		 *            The number of files
		 * @param seedCount
		 *            The number of connected seeds
		 * @param leechCount
		 *            The number of connected leechers
		 */
		public TorrentFile(String name, String size, String magnetUri, String downloadUri, int fileCount, int seedCount, int leechCount) {
			this.name = name;
			this.size = size;
			this.magnetUri = magnetUri;
			this.downloadUri = downloadUri;
			this.fileCount = fileCount;
			this.seedCount = seedCount;
			this.leechCount = leechCount;
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
		 * @return The magnet URI of the file, or {@code null} if there is no
		 *         magnet URI for this torrent file
		 */
		public String magnetUri() {
			return magnetUri;
		}

		/**
		 * Returns the download URI of the file.
		 *
		 * @return The download URI of the file, or {@code null} if there is no
		 *         download URI for this torrent file
		 */
		public String downloadUri() {
			return downloadUri;
		}

		/**
		 * Returns the number of files in this torrent.
		 *
		 * @return The number of files in this torrent
		 */
		public int fileCount() {
			return fileCount;
		}

		/**
		 * Returns the number of seeds connected to this torrent.
		 *
		 * @return The number of connected seeds
		 */
		public int seedCount() {
			return seedCount;
		}

		/**
		 * Returns the number of leechers connected to this torrent.
		 *
		 * @return The number of connected leechers
		 */
		public int leechCount() {
			return leechCount;
		}

		//
		// PRIVATE METHODS
		//

		/**
		 * Generates an ID for this file. If a {@link #magnetUri} is set, an ID
		 * is {@link #extractId(String) extracted} from it. Otherwise the magnet
		 * URI is used. If the {@link #magnetUri} is not set, the
		 * {@link #downloadUri} is used. If that is not set either, the name of
		 * the file is returned.
		 *
		 * @return The generated ID
		 */
		private String generateId() {
			if (magnetUri != null) {
				String id = extractId(magnetUri);
				if (id != null) {
					return id;
				}
				return magnetUri;
			}
			return (downloadUri != null) ? downloadUri : name;
		}

		//
		// STATIC METHODS
		//

		/**
		 * Tries to extract the “exact target” of a magnet URI.
		 *
		 * @param magnetUri
		 *            The magnet URI to extract the “xt” from
		 * @return The extracted ID, or {@code null} if no ID could be found
		 */
		private static String extractId(String magnetUri) {
			List<NameValuePair> parameters = URLEncodedUtils.parse(magnetUri.substring("magnet:?".length()), Charset.forName("UTF-8"));
			for (NameValuePair parameter : parameters) {
				if (parameter.getName().equals("xt")) {
					return parameter.getValue();
				}
			}
			return null;
		}

		//
		// OBJECT METHODS
		//

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return (generateId() != null) ? generateId().hashCode() : 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof TorrentFile)) {
				return false;
			}
			if (generateId() != null) {
				return generateId().equals(((TorrentFile) object).generateId());
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return String.format("%s(%s,%s,%s)", name(), size(), magnetUri(), downloadUri());
		}

	}

}
