/*
 * Rhynodge - KickAssTorrentsWatcher.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.watchers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.EpisodeFilter;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.KickAssTorrentsFilter;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.triggers.NewEpisodeTrigger;

import com.google.common.collect.ImmutableList;

/**
 * {@link Watcher} implementation that watches Kick Ass Torrents for new
 * episodes.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class KickAssTorrentsEpisodeWatcher extends DefaultWatcher {

	/**
	 * Creates a new Kick Ass Torrents episode watcher.
	 *
	 * @param searchTerms
	 *            The terms to search for
	 */
	public KickAssTorrentsEpisodeWatcher(String searchTerms) {
		super(createHttpQuery(searchTerms), createFilters(), createTrigger());
	}

	//
	// STATIC METHODS
	//

	/**
	 * Creates the query of the watcher.
	 *
	 * @param searchTerms
	 *            The search terms of the query
	 * @return The query of the watcher
	 */
	private static Query createHttpQuery(String searchTerms) {
		try {
			return new HttpQuery("http://kat.ph/usearch/" + URLEncoder.encode(searchTerms, "UTF-8") + "/?field=time_add&sorder=desc");
		} catch (UnsupportedEncodingException uee1) {
			/* will not happen. */
			return null;
		}
	}

	/**
	 * Creates the filters of the watcher.
	 *
	 * @return The filters of the watcher
	 */
	private static List<Filter> createFilters() {
		return ImmutableList.<Filter> of(new HtmlFilter(), new KickAssTorrentsFilter(), new EpisodeFilter());
	}

	/**
	 * Creates the trigger of the watcher.
	 *
	 * @return The trigger of the watcher
	 */
	private static Trigger createTrigger() {
		return new NewEpisodeTrigger();
	}

}
