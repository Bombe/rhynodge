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
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.states.HttpState;
import net.pterodactylus.rhynodge.states.StringState;

/**
 * {@link Filter} implementation that uses the {@link StringState#value() value}
 * of a {@link StringState} as a URL for {@link HttpQuery}, turning it into an
 * {@link HttpState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HttpQueryFilter implements Filter {

	@Override
	public State filter(State state) {
		checkArgument(state instanceof StringState, "state must be a String state");

		StringState stringState = (StringState) state;
		String url = stringState.value();

		HttpQuery query = new HttpQuery(url);
		return query.state();
	}

}
