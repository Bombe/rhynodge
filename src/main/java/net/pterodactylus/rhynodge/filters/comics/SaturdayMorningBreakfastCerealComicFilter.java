/*
 * rhynodge - SaturdayMorningBreakfastCerealComicFilter.java - Copyright © 2013 David Roden
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.jsoup.nodes.Document;

/**
 * {@link ComicSiteFilter} implementation that can parse Saturday Morning
 * Breakfast Cereal.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SaturdayMorningBreakfastCerealComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		return Optional.of("");
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		String imageUrl = document.select("#comicimage img").get(0).attr("src");
		String afterImageUrl = document.select("#aftercomic img").attr("src");
		return FluentIterable.from(Arrays.asList(imageUrl, afterImageUrl)).filter(url -> url.length() > 0).toList();
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
