/*
 * rhynodge - XkcdFilter.java - Copyright © 2013 David Roden
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

import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse XKCD comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class XkcdComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		Elements titleElement = document.select("div#ctitle");
		return titleElement.hasText() ? Optional.of(titleElement.text()) : Optional.<String>absent();
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		return extractImages(document).transform(new Function<String[], String>() {

			@Override
			public String apply(String[] input) {
				return input[0];
			}
		}).toList();
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return extractImages(document).transform(new Function<String[], String>() {

			@Override
			public String apply(String[] input) {
				return input[1];
			}
		}).toList();
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Extracts pairs of image URLs and image comments from the given document.
	 *
	 * @param document
	 * 		The document to extract the images from
	 * @return An iterable containing all image URL and comment pairs
	 */
	private FluentIterable<String[]> extractImages(Document document) {
		return FluentIterable.from(document.select("div#comic img")).transform(new Function<Element, String[]>() {

			@Override
			public String[] apply(Element image) {
				return new String[] { image.attr("src"), image.attr("title") };
			}
		});
	}

}
