/*
 * rhynodge - KevinAndKellComicFilter.java - Copyright © 2013 David Roden
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
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * {@link ComicSiteFilter} implementation that can parse Kevin and Kell comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class KevinAndKellComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		Elements captionElements = document.select("#caption");
		return Optional.fromNullable(StringEscapeUtils.unescapeHtml4(captionElements.text()));
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		Elements imageTag = document.select("#comicstrip");
		return imageTag.hasAttr("src") ? Arrays.asList(imageTag.attr("src")) : Collections.<String>emptyList();
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
