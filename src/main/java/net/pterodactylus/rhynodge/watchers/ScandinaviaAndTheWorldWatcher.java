/*
 * rhynodge - ScandinaviaAndTheWorldWatcher.java - Copyright © 2013 David Roden
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

import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.comics.ScandinaviaAndTheWorldComicFilter;
import net.pterodactylus.rhynodge.mergers.ComicMerger;
import net.pterodactylus.rhynodge.queries.HttpQuery;

import static java.util.Arrays.asList;

/**
 * {@link net.pterodactylus.rhynodge.Watcher} implementation that watches for new Scandinavia and the World comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ScandinaviaAndTheWorldWatcher extends DefaultWatcher {

	public ScandinaviaAndTheWorldWatcher() {
		super(new HttpQuery("https://satwcomic.com/latest"), asList(new HtmlFilter(), new ScandinaviaAndTheWorldComicFilter()), new ComicMerger());
	}

}
