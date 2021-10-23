package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document

class DrugsAndWiresComicFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document) =
		document.select("article.webcomic1 header.post-header h2").text().asOptional()

	override fun extractImageUrls(document: Document) =
		document.select(".webcomic-image a.current-webcomic img").map { it.attr("src") }

	override fun extractImageComments(document: Document) =
		listOf(document.select("article.webcomic1 div.post-content").text())

}
