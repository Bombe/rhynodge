package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document

class OglafComicSiteFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document) = document.title().asOptional()

	override fun extractImageUrls(document: Document) =
		document.getElementById("strip")
			.attr("src")
			.let(::listOf)

	override fun extractImageComments(document: Document) =
		document.getElementById("strip")
			.attr("title")
			.let(::listOf)

}
