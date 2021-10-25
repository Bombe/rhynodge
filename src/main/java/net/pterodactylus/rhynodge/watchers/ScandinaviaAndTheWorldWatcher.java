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

import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.filters.ExtractUrlFilter;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.HttpQueryFilter;
import net.pterodactylus.rhynodge.filters.comics.ScandinaviaAndTheWorldComicFilter;
import net.pterodactylus.rhynodge.mergers.ComicMerger;
import net.pterodactylus.rhynodge.queries.HttpQuery;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * {@link net.pterodactylus.rhynodge.Watcher} implementation that watches for new Scandinavia and the World comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ScandinaviaAndTheWorldWatcher extends DefaultWatcher {

	public ScandinaviaAndTheWorldWatcher() {
		super(new HttpQuery("http://satwcomic.com/"), createFilters(), new ComicMerger());
	}

	private static List<Filter> createFilters() {
		ImmutableList.Builder<Filter> filters = ImmutableList.builder();

		filters.add(new HtmlFilter());
		filters.add(new ExtractUrlFilter() {

			@Override
			protected Optional<String> extractUrl(Document document) {
				Elements linkTag = document.select("a.btn-success");
				return linkTag.hasAttr("href") ? Optional.of(linkTag.attr("href")) : Optional.<String>absent();
			}
		});
		filters.add(new HttpQueryFilter());
		filters.add(new HtmlFilter());
		filters.add(new ScandinaviaAndTheWorldComicFilter());

		return filters.build();
	}

}
