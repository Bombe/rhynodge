/*
 * Rhynodge - EpisodeFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.filters;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.EpisodeState;
import net.pterodactylus.rhynodge.states.EpisodeState.Episode;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Filter} implementation that extracts {@link Episode} information from
 * the {@link TorrentFile}s contained in a {@link TorrentState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeFilter implements Filter {

	private static final Logger logger = Logger.getLogger(EpisodeFilter.class);

	/** The pattern to parse episode information from the filename. */
	private static final Collection<Pattern> episodePatterns = asList(Pattern.compile("[Ss](\\d{2})[Ee](\\d{2})"), Pattern.compile("[^\\d](\\d{1,2})x(\\d{2})[^\\d]"));

	//
	// FILTER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public State filter(@NotNull State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof TorrentState, "state is not a TorrentState but a %s!", state.getClass());

		TorrentState torrentState = (TorrentState) state;
		final Multimap<Episode, TorrentFile> episodes = HashMultimap.create();
		for (TorrentFile torrentFile : torrentState) {
			Optional<Episode> episode = extractEpisode(torrentFile);
			if (!episode.isPresent()) {
				continue;
			}
			episodes.put(episode.get(), torrentFile);
		}

		return new EpisodeState(from(episodes.keySet()).transform(episodeFiller(episodes)).toSet());
	}

	//
	// STATIC METHODS
	//

	/**
	 * Returns a function that creates an {@link Episode} that contains all {@link
	 * TorrentFile}s.
	 *
	 * @param episodeTorrents
	 * 		A multimap mapping episodes to torrent files.
	 * @return The function that performs the extraction of torrent files
	 */
	private static Function<Episode, Episode> episodeFiller(final Multimap<Episode, TorrentFile> episodeTorrents) {
		return new Function<Episode, Episode>() {
			@Override
			public Episode apply(Episode episode) {
				Episode completeEpisode = new Episode(episode.season(), episode.episode());
				for (TorrentFile torrentFile : episodeTorrents.get(episode)) {
					completeEpisode.addTorrentFile(torrentFile);
				}
				return completeEpisode;
			}
		};
	}

	/**
	 * Extracts episode information from the given torrent file.
	 *
	 * @param torrentFile
	 * 		The torrent file to extract the episode information from
	 * @return The extracted episode information, or {@link Optional#absent()} if
	 *         no episode information could be found
	 */
	private static Optional<Episode> extractEpisode(TorrentFile torrentFile) {
		logger.debug(String.format("Extracting episode from %s...", torrentFile));
		for (Pattern episodePattern : episodePatterns) {
			Matcher matcher = episodePattern.matcher(torrentFile.name());
			if (!matcher.find() || matcher.groupCount() < 2) {
				continue;
			}
			String seasonString = matcher.group(1);
			String episodeString = matcher.group(2);
			logger.debug(String.format("Parsing %s and %s as season and episode...", seasonString, episodeString));
			int season = Integer.valueOf(seasonString);
			int episode = Integer.valueOf(episodeString);
			return Optional.of(new Episode(season, episode));
		}
		return absent();
	}

}
