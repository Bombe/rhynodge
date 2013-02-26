/*
 * rhynodge - HttpQueryFilter.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Preconditions.*;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.StringState;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;

/**
 * {@link Filter} implementation that extracts a URL from an {@link HtmlState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class ExtractUrlFilter implements Filter {

	@Override
	public State filter(State state) {
		checkArgument(state instanceof HtmlState, "state must be an HTML state");

		HtmlState htmlState = (HtmlState) state;
		Optional<String> newUrl = extractUrl(htmlState.document());

		if (!newUrl.isPresent()) {
			return new FailedState();
		}

		return new StringState(newUrl.get());
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * Extracts the URL from the given document. If the returned value is {@link
	 * Optional#absent()}, {@link #filter(State)} will return a {@link
	 * FailedState}.
	 *
	 * @param document
	 * 		The document to extract the URL from
	 * @return The extracted URL, or {@link Optional#absent()}
	 */
	protected abstract Optional<String> extractUrl(Document document);

}
