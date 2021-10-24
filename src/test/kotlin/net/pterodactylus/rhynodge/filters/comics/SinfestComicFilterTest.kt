package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.Filter
import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.instanceOf
import org.junit.Test

/**
 * Unit test for [SinfestComicFilter].
 */
class SinfestComicFilterTest {

	private val sinfestFilter: Filter = SinfestComicFilter()
	private val htmlState = ResourceLoader
		.loadDocument(SinfestComicFilter::class.java, "sinfest.html", "https://sinfest.xyz/")
		.let { HtmlState("https://sinfest.xyz/", it) }

	@Test
	fun canParseComicsFromHtml() {
		val state = sinfestFilter.filter(htmlState)
		assertThat(state, instanceOf(ComicState::class.java))
	}

	@Test
	fun imageUrlsAreParsedCorrectly() {
		val comicState = sinfestFilter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
			Comic("October 24, 2021: Unperson 33").add(Strip("https://sinfest.xyz/btphp/comics/2021-10-24.jpg", ""))
		))
	}

}
