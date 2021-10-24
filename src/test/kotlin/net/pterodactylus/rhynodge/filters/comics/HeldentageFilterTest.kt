package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.HtmlState
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

/**
 * Unit test for [HeldentageFilter].
 */
class HeldentageFilterTest {

	private val heldentageFilter = HeldentageFilter()
	private val htmlState = ResourceLoader
		.loadDocument(HeldentageFilter::class.java, "heldentage.html", "http://www.der-flix.de/")
		.let { HtmlState("http://www.der-flix.de/", it) }

	@Test
	fun comicIsParsedCorrectly() {
		val comicState = heldentageFilter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
				Comic("Glückskind").add(Strip("http://www.der-flix.de/media/djmediatools/cache/1-glueckskind/1500x275-towidth-100-glueckskind_301.jpg", ""))
		))
	}

}
