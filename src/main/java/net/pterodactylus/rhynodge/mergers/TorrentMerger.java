/*
 * Rhynodge - TorrentMerger.java - Copyright © 2013–2021 David Roden
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

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

import net.pterodactylus.rhynodge.Merger;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import static com.google.common.base.Preconditions.checkState;

/**
 * {@link Merger} implementation that merges two {@link TorrentState}s, taking
 * careful note of which {@link TorrentFile}s appear in the current
 * {@link TorrentState} but not in the previous one.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentMerger implements Merger {

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public State mergeStates(@Nonnull State previousState, @Nonnull State currentState) {
		checkState(currentState instanceof TorrentState, "currentState is not a TorrentState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof TorrentState, "previousState is not a TorrentState but a %s", currentState.getClass().getName());

		Set<TorrentFile> allTorrentFiles = new HashSet<>(((TorrentState) previousState).torrentFiles());
		Set<TorrentFile> newTorrentFiles = new HashSet<>();
		for (TorrentFile torrentFile : (TorrentState) currentState) {
			if (allTorrentFiles.add(torrentFile)) {
				newTorrentFiles.add(torrentFile);
			}
		}

		return new TorrentState(allTorrentFiles, newTorrentFiles);
	}

}