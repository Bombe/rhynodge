/*
 * rhynodge - Collar6Watcher.java - Copyright © 2013 David Roden
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

import java.util.Arrays;

import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.comics.Collar6ComicFilter;
import net.pterodactylus.rhynodge.mergers.ComicMerger;
import net.pterodactylus.rhynodge.queries.HttpQuery;

/**
 * {@link Watcher} implementation that watches for new Collar 6 comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Collar6Watcher extends DefaultWatcher {

	/** Creates a new watcher for Cyanide and Happiness comics. */
	public Collar6Watcher() {
		super(new HttpQuery("http://collar6.com/"), Arrays.asList(new HtmlFilter(), new Collar6ComicFilter()), new ComicMerger());
	}

}
