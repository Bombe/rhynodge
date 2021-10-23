package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

class DrugsAndWiresComicFilterTest {

	private val filter = DrugsAndWiresComicFilter()
	private val htmlState: HtmlState = ResourceLoader
		.loadDocument(javaClass, "drugs-and-wires.html", baseUrl)
		.let { HtmlState(baseUrl, it) }

	@Test
	fun `filter can extract comic correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(
			comicState.comics(),
			contains(
				Comic("Chapter 8 Page 18").add(Strip("https://www.drugsandwires.fail/wp-content/uploads/2021/10/18.jpg", ""))
			)
		)
	}

}

private const val baseUrl = "https://www.drugsandwires.fail/"
