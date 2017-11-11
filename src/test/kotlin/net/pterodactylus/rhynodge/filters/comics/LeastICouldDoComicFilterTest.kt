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
 * Unit test for [LeastICouldDoComicFilterTest].
 */
class LeastICouldDoComicFilterTest {

	private val filter = LeastICouldDoComicFilter()
	private val htmlState: HtmlState = ResourceLoader
			.loadDocument(javaClass, "least-i-could-do.html", baseUrl)
			.let { HtmlState(baseUrl, it) }

	@Test
	fun `comic is extracted correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
				Comic("").
						add(Strip("http://leasticoulddo.com/wp-content/uploads/2017/11/528B9685-DA9C-4320-8AC6-C6871DF81C3F.jpeg", ""))
		))
	}

}

private val baseUrl = "http://leasticoulddo.com/comic/20171111"
