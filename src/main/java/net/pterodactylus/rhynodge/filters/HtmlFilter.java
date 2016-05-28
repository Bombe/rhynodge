/*
 * Rhynodge - HtmlFilter.java - Copyright © 2013 David Roden
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

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.HttpState;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * {@link Filter} that converts a {@link HttpState} into an {@link HtmlState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HtmlFilter implements Filter {

	private static final Logger logger = Logger.getLogger(HtmlFilter.class);

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public State filter(@NotNull State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof HttpState, "state is not a HttpState but a %s", state.getClass().getName());
		logger.trace(String.format("Got HTML: %s, %s", ((HttpState) state).contentType(), ((HttpState) state).content()));
		Document document = Jsoup.parse(((HttpState) state).content(), ((HttpState) state).uri());
		return new HtmlState(((HttpState) state).uri(), document);
	}

}
