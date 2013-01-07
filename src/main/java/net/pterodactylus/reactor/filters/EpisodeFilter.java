/*
 * Reactor - EpisodeFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.filters;

import static com.google.common.base.Preconditions.checkState;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.pterodactylus.reactor.Filter;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.states.EpisodeState;
import net.pterodactylus.reactor.states.EpisodeState.Episode;
import net.pterodactylus.reactor.states.FailedState;
import net.pterodactylus.reactor.states.TorrentState;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

/**
 * {@link Filter} implementation that extracts {@link Episode} information from
 * the {@link TorrentFile}s contained in a {@link TorrentState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeFilter implements Filter {

	/** The pattern to parse episode information from the filename. */
	private static Pattern episodePattern = Pattern.compile("S(\\d{2})E(\\d{2})|[^\\d](\\d{1,2})x(\\d{2})[^\\d]");

	//
	// FILTER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State filter(State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof TorrentState, "state is not a TorrentState but a %s!", state.getClass());

		TorrentState torrentState = (TorrentState) state;
		LinkedHashMap<Episode, Episode> episodes = new LinkedHashMap<Episode, Episode>();
		for (TorrentFile torrentFile : torrentState) {
			Episode episode = extractEpisode(torrentFile);
			if (episode == null) {
				continue;
			}
			episodes.put(episode, episode);
			episode = episodes.get(episode);
			episode.addTorrentFile(torrentFile);
		}

		return new EpisodeState(episodes.values());
	}

	//
	// STATIC METHODS
	//

	/**
	 * Extracts episode information from the given torrent file.
	 *
	 * @param torrentFile
	 *            The torrent file to extract the episode information from
	 * @return The extracted episode information, or {@code null} if no episode
	 *         information could be found
	 */
	private static Episode extractEpisode(TorrentFile torrentFile) {
		Matcher matcher = episodePattern.matcher(torrentFile.name());
		if (!matcher.find()) {
			return null;
		}
		String seasonString = matcher.group(1);
		String episodeString = matcher.group(2);
		if ((seasonString == null) && (episodeString == null)) {
			seasonString = matcher.group(3);
			episodeString = matcher.group(4);
		}
		int season = Integer.valueOf(seasonString);
		int episode = Integer.valueOf(episodeString);
		return new Episode(season, episode);
	}

}
