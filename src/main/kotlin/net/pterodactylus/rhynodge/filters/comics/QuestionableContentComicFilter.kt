/*
 * rhynodge - QuestionableContentComicFilter.kt - Copyright © 2013–2020 David Roden
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
import org.jsoup.nodes.Document

class QuestionableContentComicFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> = Optional.absent()

	override fun extractImageUrls(document: Document): List<String> =
			listOf(document.select("#comicnav").first().nextSibling().nextSibling().attr("src"))

	override fun extractImageComments(document: Document): List<String> = emptyList()

}
