package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

class SoggyCardboardComicFilterTest {

	private val filter = SoggyCardboardComicFilter()
	private val htmlState: HtmlState = ResourceLoader
			.loadDocument(javaClass, "soggy-cardboard.html", baseUrl)
			.let { HtmlState(baseUrl, it) }

	@Test
	fun `comic is extracted correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
				Comic("#082 Pet Sedentary").add(Strip("http://www.soggycardboard.com/wp-content/uploads/2020/09/082.png", "That like how the Grand Canyon was made. But in reverse"))
		))
	}

}

private val baseUrl = "http://www.soggycardboard.com/"
