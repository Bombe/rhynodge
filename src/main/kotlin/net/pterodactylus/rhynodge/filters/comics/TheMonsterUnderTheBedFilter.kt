package net.pterodactylus.rhynodge.filters.comics

import com.google.common.base.*
import com.google.common.base.Optional.*
import net.pterodactylus.rhynodge.filters.*
import org.jsoup.nodes.*

class TheMonsterUnderTheBedFilter : ComicSiteFilter() {

	override fun extractTitle(document: Document): Optional<String> =
			document.select("h2.post-title").text().toOptional()

	override fun extractImageUrls(document: Document): List<String> =
			document.select("div#comic img").map { it.attr("src") }

	override fun extractImageComments(document: Document): List<String> = emptyList()

}

private fun <T> T?.toOptional(): Optional<T> = fromNullable(this)
