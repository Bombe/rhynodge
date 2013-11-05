/*
 * rhynodge - GeneralProtectionFaultComicFilter.java - Copyright © 2013 David Roden
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

import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse General Protection
 * Fault comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class GeneralProtectionFaultComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		return Optional.of("");
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		Elements imageElements = document.select(".content img[alt~=.Comic.for]");
		return from(imageElements).transformAndConcat(new Function<Element, Iterable<String>>() {
			@Override
			public Iterable<String> apply(Element element) {
				return ((element != null) && element.hasAttr("src")) ? asList(element.attr("src")) : Collections.<String>emptyList();
			}
		}).toList();
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return emptyList();
	}

}
