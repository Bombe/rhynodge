/*
 * Rhynodge - EpisodeState.java - Copyright © 2013 David Roden
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.filters.EpisodeFilter;
import net.pterodactylus.rhynodge.states.EpisodeState.Episode;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * {@link State} implementation that stores episodes of TV shows, parsed via
 * {@link EpisodeFilter} from a previous {@link TorrentState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeState extends AbstractState implements Iterable<Episode> {

	/**
	 * The episodes found in the current request.
	 */
	@JsonProperty
	private final List<Episode> episodes = new ArrayList<>();
	private final Set<Episode> newEpisodes = new HashSet<>();
	private final Set<Episode> changedEpisodes = new HashSet<>();
	private final Set<TorrentFile> newTorrentFiles = new HashSet<>();

	/**
	 * No-arg constructor for deserialization.
	 */
	@SuppressWarnings("unused")
	private EpisodeState() {
	}

	/**
	 * Creates a new episode state.
	 *
	 * @param episodes The episodes of the request
	 */
	public EpisodeState(Collection<Episode> episodes) {
		this.episodes.addAll(episodes);
	}

	public EpisodeState(Collection<Episode> episodes, Collection<Episode> newEpisodes, Collection<Episode> changedEpisodes, Collection<TorrentFile> newTorreFiles) {
		this(episodes);
		this.newEpisodes.addAll(newEpisodes);
		this.changedEpisodes.addAll(changedEpisodes);
		this.newTorrentFiles.addAll(newTorreFiles);
	}

	//
	// ACCESSORS
	//

	@Override
	public boolean isEmpty() {
		return episodes.isEmpty();
	}

	/**
	 * Returns all episodes contained in this state.
	 *
	 * @return The episodes of this state
	 */
	public Collection<Episode> episodes() {
		return Collections.unmodifiableCollection(episodes);
	}

	@Nonnull
	@Override
	protected String summary(Reaction reaction) {
		if (!newEpisodes.isEmpty()) {
			if (!changedEpisodes.isEmpty()) {
				return String.format("%d new and %d changed Torrent(s) for “%s!”", newEpisodes.size(), changedEpisodes.size(), reaction.name());
			}
			return String.format("%d new Torrent(s) for “%s!”", newEpisodes.size(), reaction.name());
		}
		return String.format("%d changed Torrent(s) for “%s!”", changedEpisodes.size(), reaction.name());
	}

	@Nonnull
	@Override
	protected String plainText() {
		StringBuilder stringBuilder = new StringBuilder();
		if (!newEpisodes.isEmpty()) {
			stringBuilder.append("New Episodes\n\n");
			for (Episode episode : newEpisodes) {
				stringBuilder.append("- ").append(episode.identifier()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("  - ").append(torrentFile.name()).append(", ").append(torrentFile.size()).append("\n");
					if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
						stringBuilder.append("    Magnet: ").append(torrentFile.magnetUri()).append("\n");
					}
					if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
						stringBuilder.append("    Download: ").append(torrentFile.downloadUri()).append("\n");
					}
				}
			}
		}
		if (!changedEpisodes.isEmpty()) {
			stringBuilder.append("Changed Episodes\n\n");
			for (Episode episode : changedEpisodes) {
				stringBuilder.append("- ").append(episode.identifier()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("  - ").append(torrentFile.name()).append(", ").append(torrentFile.size()).append("\n");
					if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
						stringBuilder.append("    Magnet: ").append(torrentFile.magnetUri()).append("\n");
					}
					if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
						stringBuilder.append("    Download: ").append(torrentFile.downloadUri()).append("\n");
					}
				}
			}
		}
		/* list all known episodes. */
		stringBuilder.append("All Known Episodes\n\n");
		ImmutableMap<Integer, Collection<Episode>> episodesBySeason = FluentIterable.from(episodes).index(Episode::season).asMap();
		for (Map.Entry<Integer, Collection<Episode>> seasonEntry : episodesBySeason.entrySet()) {
			stringBuilder.append("  Season ").append(seasonEntry.getKey()).append("\n\n");
			for (Episode episode : Ordering.natural().sortedCopy(seasonEntry.getValue())) {
				stringBuilder.append("    Episode ").append(episode.episode()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("      Size: ").append(torrentFile.size());
					stringBuilder.append(" in ").append(torrentFile.fileCount()).append(" file(s): ");
					stringBuilder.append(torrentFile.magnetUri());
				}
			}
		}
		return stringBuilder.toString();
	}

	@Nullable
	@Override
	protected String htmlText() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>\n");
		/* show all known episodes. */
		htmlBuilder.append("<table>\n<caption>All Known Episodes</caption>\n");
		htmlBuilder.append("<thead>\n");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<th>Season</th>");
		htmlBuilder.append("<th>Episode</th>");
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
		Episode lastEpisode = null;
		for (Map.Entry<Integer, Collection<Episode>> seasonEntry : FluentIterable.from(Ordering.natural().reverse().sortedCopy(episodes)).index(Episode.BY_SEASON).asMap().entrySet()) {
			for (Episode episode : seasonEntry.getValue()) {
				for (TorrentFile torrentFile : episode) {
					if (newEpisodes.contains(episode)) {
						htmlBuilder.append("<tr style=\"color: #008000; font-weight: bold;\">");
					} else if (newTorrentFiles.contains(torrentFile)) {
						htmlBuilder.append("<tr style=\"color: #008000;\">");
					} else {
						htmlBuilder.append("<tr>");
					}
					if ((lastEpisode == null) || !lastEpisode.equals(episode)) {
						htmlBuilder.append("<td>").append(episode.season()).append("</td><td>").append(episode.episode()).append("</td>");
					} else {
						htmlBuilder.append("<td colspan=\"2\"></td>");
					}
					htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.name())).append("</td>");
					htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.size())).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.fileCount()).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.seedCount()).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.leechCount()).append("</td>");
					htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.magnetUri())).append("\">Link</a></td>");
					htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.downloadUri())).append("\">Link</a></td>");
					htmlBuilder.append("</tr>\n");
					lastEpisode = episode;
				}
			}
		}
		htmlBuilder.append("</tbody>\n");
		htmlBuilder.append("</table>\n");
		htmlBuilder.append("</body></html>\n");
		return htmlBuilder.toString();
	}

	//
	// ITERABLE INTERFACE
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Episode> iterator() {
		return episodes.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[episodes=%s]", getClass().getSimpleName(), episodes);
	}

	/**
	 * Stores attributes for an episode.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Episode implements Comparable<Episode>, Iterable<TorrentFile> {

		/** Function to extract the season of an episode. */
		public static final Function<Episode, Integer> BY_SEASON = new Function<Episode, Integer>() {

			@Override
			public Integer apply(Episode episode) {
				return (episode != null ) ? episode.season() : -1;
			}
		};

		/** The season of the episode. */
		@JsonProperty
		private final int season;

		/** The number of the episode. */
		@JsonProperty
		private final int episode;

		/** The torrent files for this episode. */
		@JsonProperty
		private final List<TorrentFile> torrentFiles = new ArrayList<TorrentFile>();

		/**
		 * No-arg constructor for deserialization.
		 */
		@SuppressWarnings("unused")
		private Episode() {
			this(0, 0);
		}

		/**
		 * Creates a new episode.
		 *
		 * @param season
		 *            The season of the episode
		 * @param episode
		 *            The number of the episode
		 */
		public Episode(int season, int episode) {
			this.season = season;
			this.episode = episode;
		}

		//
		// ACCESSORS
		//

		/**
		 * Returns the season of this episode.
		 *
		 * @return The season of this episode
		 */
		public int season() {
			return season;
		}

		/**
		 * Returns the number of this episode.
		 *
		 * @return The number of this episode
		 */
		public int episode() {
			return episode;
		}

		/**
		 * Returns the torrent files of this episode.
		 *
		 * @return The torrent files of this episode
		 */
		public Collection<TorrentFile> torrentFiles() {
			return torrentFiles;
		}

		/**
		 * Returns the identifier of this episode.
		 *
		 * @return The identifier of this episode
		 */
		public String identifier() {
			return String.format("S%02dE%02d", season, episode);
		}

		//
		// ACTIONS
		//

		/**
		 * Adds the given torrent file to this episode.
		 *
		 * @param torrentFile
		 *            The torrent file to add
		 */
		public void addTorrentFile(TorrentFile torrentFile) {
			if (!torrentFiles.contains(torrentFile)) {
				torrentFiles.add(torrentFile);
			}
		}

		//
		// ITERABLE METHODS
		//

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<TorrentFile> iterator() {
			return torrentFiles.iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Episode episode) {
			if (season() < episode.season()) {
				return -1;
			}
			if (season() > episode.season()) {
				return 1;
			}
			if (episode() < episode.episode()) {
				return -1;
			}
			if (episode() > episode.episode()) {
				return 1;
			}
			return 0;
		}

		//
		// OBJECT METHODS
		//

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return season * 65536 + episode;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Episode)) {
				return false;
			}
			Episode episode = (Episode) obj;
			return (season == episode.season) && (this.episode == episode.episode);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return String.format("%s[season=%d,episode=%d,torrentFiles=%s]", getClass().getSimpleName(), season, episode, torrentFiles);
		}

	}

}
