/*
 * rhynodge - SinfestComicFilter.java - Copyright © 2013–2021 David Roden
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
package net.pterodactylus.rhynodge.filters.comics

import com.google.common.base.Optional
import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.stream.Collectors

/**
 * [ComicSiteFilter] implementation that can parse Sinfest.
 */
class SinfestComicFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> =
		document.select("tr.style5 td.style3").text().asOptional()

	override fun extractImageUrls(document: Document): List<String> =
		document.select("div.container img")
			.map { it.attr("src") }

	override fun extractImageComments(document: Document): List<String> =
		emptyList()

}