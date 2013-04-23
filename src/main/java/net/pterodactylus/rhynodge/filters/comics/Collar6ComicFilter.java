/*
 * rhynodge - Collar6ComicFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.filters.comics;

import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse Collar 6 comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Collar6ComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		return Optional.of("");
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		Elements imageTags = document.select(".webcomic-object-full img");
		return FluentIterable.from(imageTags).transform(new Function<Element, String>() {

			@Override
			public String apply(Element input) {
				return input.attr("src");
			}
		}).toList();
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
