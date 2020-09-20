package net.pterodactylus.rhynodge.filters.comics

import com.google.common.base.Optional
import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document

class BusinessCatComicFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> =
			document.select(".comic-title h2").text().asOptional()

	override fun extractImageUrls(document: Document): List<String> =
			listOf(document.select(".comic-image img").attr("src"))

	override fun extractImageComments(document: Document): List<String> = emptyList()

}
