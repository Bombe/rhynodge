/*
 * Reactor - NewTorrentTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.triggers;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.TorrentState;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

import com.google.common.collect.Lists;

/**
 * {@link Trigger} implementation that is triggered by {@link TorrentFile}s that
 * appear in the current {@link TorrentState} but not in the previous one.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewTorrentTrigger implements Trigger {

	/** The newly detected torrent files. */
	private List<TorrentFile> torrentFiles = Lists.newArrayList();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(State currentState, State previousState) {
		checkState(currentState instanceof TorrentState, "currentState is not a TorrentState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof TorrentState, "previousState is not a TorrentState but a %s", currentState.getClass().getName());
		TorrentState currentTorrentState = (TorrentState) currentState;
		TorrentState previousTorrentState = (TorrentState) previousState;
		torrentFiles.clear();
		for (TorrentFile torrentFile : currentTorrentState) {
			torrentFiles.add(torrentFile);
		}
		for (TorrentFile torrentFile : previousTorrentState) {
			torrentFiles.remove(torrentFile);
		}
		return !torrentFiles.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object trigger() {
		return torrentFiles;
	}

}
