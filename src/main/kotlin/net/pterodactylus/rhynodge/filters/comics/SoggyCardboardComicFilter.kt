package net.pterodactylus.rhynodge.filters.comics

import com.google.common.base.Optional
import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.utils.asOptional
import org.jsoup.nodes.Document

class SoggyCardboardComicFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> =
			document.select(".ceo_latest_comics_widget li").first().text().asOptional()

	override fun extractImageUrls(document: Document): List<String> =
			listOf(document.select("#comic img").attr("src"))

	override fun extractImageComments(document: Document): List<String> =
			listOf(document.select("#comic img").attr("alt"))

}
