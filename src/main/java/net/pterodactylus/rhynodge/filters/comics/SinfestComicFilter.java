/*
 * rhynodge - SinfestComicFilter.java - Copyright © 2013 David Roden
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
import java.util.stream.Collectors;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse Sinfest.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SinfestComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		Elements imageCell = selectImageAttributes(document);
		return imageCell.hasAttr("alt") ? Optional.of(imageCell.attr("alt")) : Optional.<String>absent();
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		Elements imageCells = selectImageAttributes(document);
		return imageCells.stream().map(cell -> cell.attr("src")).collect(Collectors.toList());
	}

	private Elements selectImageAttributes(Document document) {
		return document.select("tbody.style5 img");
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
