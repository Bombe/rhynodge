package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

/**
 * Unit test for [CtrlAltDelComicFilter].
 */
class CtrlAltDelComicFilterTest {

	private val comicFilter = CtrlAltDelComicFilter()
	private val htmlState = ResourceLoader
		.loadDocument(CtrlAltDelComicFilter::class.java, "ctrl-alt-del.html", "https://cad-comic.com/")
		.let { HtmlState("https://cad-comic.com/", it) }

	@Test
	fun `filter can parse comic correctly`() {
		val comicState = comicFilter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
			Comic("The Starcaster Chronicles 09.14").add(Strip("https://cad-comic.com/wp-content/uploads/2021/10/09.14a-1.x93667.png", ""))
		))
	}

}
