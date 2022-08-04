package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import net.pterodactylus.rhynodge.utils.sourceAttribute
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class AdventuresWithEggieComicSiteFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document) =
		document.select(".comments-title span").text().asOptional()

	override fun extractImageUrls(document: Document) =
		document.select("#one-comic-option img.size-full").map(Element::sourceAttribute)

	override fun extractImageComments(document: Document) = emptyList<String>()

}
