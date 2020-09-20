package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

class QuestionableContentFilterTest {

	private val filter = QuestionableContentComicFilter()
	private val htmlState: HtmlState = ResourceLoader
			.loadDocument(javaClass, "questionable-content.html", baseUrl)
			.let { HtmlState(baseUrl, it) }

	@Test
	fun `comic is extracted correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
				Comic("").add(Strip("https://www.questionablecontent.net/comics/4355.png", ""))
		))
	}

}

private const val baseUrl = "https://www.questionablecontent.net/"
