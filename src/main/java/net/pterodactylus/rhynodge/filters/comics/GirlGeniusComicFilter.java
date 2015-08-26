/*
 * rhynodge - GirlGeniusComicFilter.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse Girl Genius comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class GirlGeniusComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		return extractImageUrls(document).isEmpty() ? absent() : of("");
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		Elements imageElements = document.select("#comicbody img[alt=Comic]");
		return imageElements.stream().filter(e -> e.hasAttr("src")).map(e -> e.attr("src")).collect(Collectors.toList());
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
