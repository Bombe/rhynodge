/*
 * rhynodge - BlacklistFilter.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import com.google.common.base.Predicate;

/**
 * Filter for {@link TorrentState}s that removes all {@link TorrentFile}s whose
 * names match a list of bad words.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BlacklistFilter implements Filter {

	private final Iterable<String> filterWords;

	public BlacklistFilter(List<String> filterWords) {
		this.filterWords = filterWords;
	}

	@Override
	public State filter(State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof TorrentState, "state is not a TorrentState but a %s!", state.getClass());

		TorrentState torrentState = (TorrentState) state;
		return new TorrentState(from(torrentState.torrentFiles()).filter(new Predicate<TorrentFile>() {
			@Override
			public boolean apply(TorrentFile torrentFile) {
				return (torrentFile != null) && nameDoesNotMatchAFilterWord(torrentFile.name());
			}

			private boolean nameDoesNotMatchAFilterWord(final String name) {
				return !from(filterWords).anyMatch(new Predicate<String>() {
					@Override
					public boolean apply(String word) {
						return name.toLowerCase().contains(word.toLowerCase());
					}
				});
			}
		}).toList());
	}

	public static BlacklistFilter createDefaultBlacklistFilter() {
		return new BlacklistFilter(asList(
            "[G2G]",
            "-3LT0N",
            "-ADTRG",
            "-AMIABLE",
            "-AN0NYM0US",
            "-ARROW",
            "-AQOS",
            "-AXED",
            "-BeStDivX",
            "-BiDA",
            "-CM",
            "-COCAIN",
            "-DASH",
            "-DEPRiVED",
            "-DiAMOND",
            "-DiRTYMARY",
            "-DoNE",
            "-EDAW",
            "-EVO",
            "-EVOLVE",
            "-EwDp",
            "-Felony",
            "-FooKaS",
            "-Haggebulle",
            "-HELLRAZ0R",
            "-iJUGGA",
            "-IMAGiNE",
            "-iMBT",
            "-iND",
            "-juggs",
            "-KILLERS",
            "-Larceny",
            "-LEGi0N",
            "-MAX",
            "-MiLLENiUM",
            "-NeDiVx",
            "-NoGRP",
            "-NOiR",
            "-NYDIC",
            "-P2P",
            "-PLAYNOW",
            "-PUKKA",
            "-PrisM",
            "-RARBG",
            "-S4A",
            "-SANTi",
            "-SHODAN",
            "-SPARKS",
            "-SUFFiCE",
            "-TAMILROCKERS",
            "-TARGET",
            "-TASTE",
            "-TiTAN",
            "-THC",
            "-UnKnOwN",
            "-UNiQUE",
            "-W00D"
        ));
	}

}
