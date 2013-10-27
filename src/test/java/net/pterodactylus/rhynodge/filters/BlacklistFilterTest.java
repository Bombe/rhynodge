/*
 * rhynodge - BlacklistFilterTest.java - Copyright © 2013 David Roden
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

import static java.util.Arrays.asList;
import static net.pterodactylus.rhynodge.filters.BlacklistFilter.createDefaultBlacklistFilter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.junit.Test;

/**
 * Unit test for {@link BlacklistFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BlacklistFilterTest {

	private final BlacklistFilter blacklistFilter = new BlacklistFilter(asList("-Haggebulle", "-FooKaS"));
	private final TorrentFile notMatchingTorrentFile1 = new TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-3LT0N", "1.1G", "magnet:?xt=a", "http://", 1, 2, 3);
	private final TorrentFile notMatchingTorrentFile2 = new TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-HELLRAZ0R", "1.1G", "magnet:?xt=b", "http://", 1, 2, 3);
	private final TorrentFile matchingTorrentFile1 = new TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-Haggebulle", "1.1G", "magnet:?xt=c", "http://", 1, 2, 3);
	private final TorrentFile matchingTorrentFile2 = new TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-FooKaS", "1.1G", "magnet:?xt=d", "http://", 1, 2, 3);

	@Test
	public void verifyThatMatchingTorrentsAreRemoved() {
		TorrentState torrentState = new TorrentState();
		torrentState.addTorrentFile(matchingTorrentFile1);
		torrentState.addTorrentFile(notMatchingTorrentFile1);
		torrentState.addTorrentFile(matchingTorrentFile2);
		torrentState.addTorrentFile(notMatchingTorrentFile2);
		TorrentState filteredState = (TorrentState) blacklistFilter.filter(torrentState);
		assertThat(filteredState, notNullValue());
		assertThat(filteredState.torrentFiles(), containsInAnyOrder(notMatchingTorrentFile1, notMatchingTorrentFile2));
	}

	@Test
	public void verifyThatAFailedStateResultsInAFailedState() {
		State state = blacklistFilter.filter(new FailedState());
		assertThat(state, notNullValue());
		assertThat(state.success(), is(false));
	}

	@Test
	public void verifyThatABlacklistFilterIsCreated() {
		assertThat(createDefaultBlacklistFilter(), notNullValue());
	}

}
