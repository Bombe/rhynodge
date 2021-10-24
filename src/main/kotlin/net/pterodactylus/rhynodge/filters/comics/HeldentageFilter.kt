package net.pterodactylus.rhynodge.filters.comics

import com.google.common.base.Optional
import com.google.common.base.Optional.absent
import com.google.common.base.Optional.fromNullable
import com.google.common.base.Optional.of
import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.stream.Collectors

/**
 * [ComicSiteFilter] implementation that can parse “Heldentage” comics.
 */
class HeldentageFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> =
		document.select("article.art-post h2").text().asOptional()

	override fun extractImageUrls(document: Document): List<String> =
		document.select("article.art-post img.dj-image")
			.map { it.attr("data-src") }

	override fun extractImageComments(document: Document): List<String> =
		emptyList()

}
