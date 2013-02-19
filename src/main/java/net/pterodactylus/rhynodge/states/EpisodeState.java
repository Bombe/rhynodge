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
import java.util.Iterator;
import java.util.List;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.filters.EpisodeFilter;
import net.pterodactylus.rhynodge.states.EpisodeState.Episode;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;

/**
 * {@link State} implementation that stores episodes of TV shows, parsed via
 * {@link EpisodeFilter} from a previous {@link TorrentState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeState extends AbstractState implements Iterable<Episode> {

	/** The episodes found in the current request. */
	@JsonProperty
	private final List<Episode> episodes = new ArrayList<Episode>();

	/**
	 * No-arg constructor for deserialization.
	 */
	@SuppressWarnings("unused")
	private EpisodeState() {
		this(Collections.<Episode> emptySet());
	}

	/**
	 * Creates a new episode state.
	 *
	 * @param episodes
	 *            The episodes of the request
	 */
	public EpisodeState(Collection<Episode> episodes) {
		this.episodes.addAll(episodes);
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns all episodes contained in this state.
	 *
	 * @return The episodes of this state
	 */
	public Collection<Episode> episodes() {
		return Collections.unmodifiableCollection(episodes);
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
				return episode.season();
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
