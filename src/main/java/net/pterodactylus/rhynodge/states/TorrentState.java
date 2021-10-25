/*
 * Rhynodge - TorrentState.java - Copyright © 2013 David Roden
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

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.collect.Ordering.from;
import static java.lang.String.format;

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

	private final Set<TorrentFile> newTorrentFiles = new HashSet<>();

	/**
	 * Creates a new torrent state without torrent files.
	 */
	public TorrentState() {
		this(Collections.<TorrentFile>emptySet());
	}

	/**
	 * Creates a new torrent state containing the given torrent files.
	 *
	 * @param torrentFiles
	 *            The torrent files
	 */
	public TorrentState(Collection<TorrentFile> torrentFiles) {
		files.addAll(torrentFiles);
	}

	public TorrentState(Collection<TorrentFile> torrentFiles, Collection<TorrentFile> newTorrentFiles) {
		files.addAll(torrentFiles);
		this.newTorrentFiles.addAll(newTorrentFiles);
	}

	//
	// ACCESSORS
	//

	@Override
	public boolean isEmpty() {
		return files.isEmpty();
	}

	@Override
	public boolean triggered() {
		return !newTorrentFiles.isEmpty();
	}

	/**
	 * Returns all torrent files of this state.
	 *
	 * @return All torrent files of this state
	 */
	public Collection<TorrentFile> torrentFiles() {
		return Collections.unmodifiableList(files);
	}

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

	@Nonnull
	@Override
	protected String summary(Reaction reaction) {
		return format("Found %d new Torrent(s) for “%s!”", newTorrentFiles.size(), reaction.name());
	}

	@Nonnull
	@Override
	protected String plainText() {
		StringBuilder plainText = new StringBuilder();
		plainText.append("New Torrents:\n\n");
		for (TorrentFile torrentFile : newTorrentFiles) {
			plainText.append(torrentFile.name()).append('\n');
			plainText.append('\t').append(torrentFile.size()).append(" in ").append(torrentFile.fileCount()).append(" file(s)\n");
			plainText.append('\t').append(torrentFile.seedCount()).append(" seed(s), ").append(torrentFile.leechCount()).append(" leecher(s)\n");
			if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
				plainText.append('\t').append(torrentFile.magnetUri()).append('\n');
			}
			if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
				plainText.append('\t').append(torrentFile.downloadUri()).append('\n');
			}
			plainText.append('\n');
		}
		return plainText.toString();
	}

	@Nullable
	@Override
	protected String htmlText() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>\n");
		htmlBuilder.append("<table>\n<caption>All Known Torrents</caption>\n");
		htmlBuilder.append("<thead>\n");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<th>Filename</th>");
		htmlBuilder.append("<th>Size</th>");
		htmlBuilder.append("<th>File(s)</th>");
		htmlBuilder.append("<th>Seeds</th>");
		htmlBuilder.append("<th>Leechers</th>");
		htmlBuilder.append("<th>Magnet</th>");
		htmlBuilder.append("<th>Download</th>");
		htmlBuilder.append("</tr>\n");
		htmlBuilder.append("</thead>\n");
		htmlBuilder.append("<tbody>\n");
		for (TorrentFile torrentFile : sortNewFirst().sortedCopy(files)) {
			if (newTorrentFiles.contains(torrentFile)) {
				htmlBuilder.append("<tr style=\"color: #008000; font-weight: bold;\">");
			} else {
				htmlBuilder.append("<tr>");
			}
			htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.name())).append("</td>");
			htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.size())).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.fileCount()).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.seedCount()).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.leechCount()).append("</td>");
			htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.magnetUri())).append("\">Link</a></td>");
			htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.downloadUri())).append("\">Link</a></td>");
			htmlBuilder.append("</tr>\n");
		}
		htmlBuilder.append("</tbody>\n");
		htmlBuilder.append("</table>\n");
		htmlBuilder.append("</body></html>\n");
		return htmlBuilder.toString();
	}

	/**
	 * Returns an ordering that sorts torrent files by whether they are new
	 * (according to {@link #files}) or not. New files will be sorted
	 * first.
	 *
	 * @return An ordering for “new files first”
	 */
	private Ordering<TorrentFile> sortNewFirst() {
		return from((TorrentFile leftTorrentFile, TorrentFile rightTorrentFile) -> {
			if (newTorrentFiles.contains(leftTorrentFile) && !newTorrentFiles.contains(rightTorrentFile)) {
				return -1;
			}
			if (!newTorrentFiles.contains(leftTorrentFile) && newTorrentFiles.contains(rightTorrentFile)) {
				return 1;
			}
			return 0;
		});
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
		return format("%s[files=%s]", getClass().getSimpleName(), files);
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
				return extractId(magnetUri).orElse(magnetUri);
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
		private static Optional<String> extractId(String magnetUri) {
			if ((magnetUri == null) || (magnetUri.length() < 8)) {
				return Optional.empty();
			}
			List<NameValuePair> parameters = URLEncodedUtils.parse(magnetUri.substring("magnet:?".length()), StandardCharsets.UTF_8);
			for (NameValuePair parameter : parameters) {
				if (parameter.getName().equals("xt")) {
					return Optional.of(parameter.getValue().toLowerCase());
				}
			}
			return Optional.empty();
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
			return format("%s(%s,%s,%s)", name(), size(), magnetUri(), downloadUri());
		}

	}

}
