/*
 * Rhynodge - EpisodeMerger.java - Copyright © 2013–2021 David Roden
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

package net.pterodactylus.rhynodge.mergers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nonnull;

import net.pterodactylus.rhynodge.Merger;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.EpisodeState;
import net.pterodactylus.rhynodge.states.EpisodeState.Episode;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import static com.google.common.base.Preconditions.checkState;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * {@link Merger} implementation that merges two {@link EpisodeState}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeMerger implements Merger {

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public State mergeStates(@Nonnull State previousState, @Nonnull State currentState) {
		checkState(currentState instanceof EpisodeState, "currentState is not a EpisodeState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof EpisodeState, "previousState is not a EpisodeState but a %s", currentState.getClass().getName());

		Collection<Episode> newEpisodes = new HashSet<>();
		Collection<Episode> changedEpisodes = new HashSet<>();
		Collection<TorrentFile> newTorrentFiles = new HashSet<>();
		Map<Episode, Episode> allEpisodes = ((EpisodeState) previousState).episodes().stream().collect(toMap(identity(), identity()));
		for (Episode episode : ((EpisodeState) currentState).episodes()) {
			if (!allEpisodes.containsKey(episode)) {
				allEpisodes.put(episode, episode);
				newEpisodes.add(episode);
			}
			Episode existingEpisode = allEpisodes.get(episode);
			for (TorrentFile torrentFile : new ArrayList<>(episode.torrentFiles())) {
				int oldSize = existingEpisode.torrentFiles().size();
				existingEpisode.addTorrentFile(torrentFile);
				int newSize = existingEpisode.torrentFiles().size();
				if (oldSize != newSize) {
					newTorrentFiles.add(torrentFile);
				}
				if (!newEpisodes.contains(existingEpisode) && (oldSize != newSize)) {
					changedEpisodes.add(existingEpisode);
				}
			}
		}
		return new EpisodeState(allEpisodes.values(), newEpisodes, changedEpisodes, newTorrentFiles);
	}

}
