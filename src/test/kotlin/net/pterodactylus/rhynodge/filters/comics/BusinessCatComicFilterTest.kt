package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

/**
 * Unit test for [BusinessCatComicFilter].
 */
class BusinessCatComicFilterTest {

	@Test
	fun `comic is extracted correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), contains(
				ComicState.Comic("Full Circle").add(ComicState.Strip("https://www.businesscatcomic.com/wp-content/uploads/2019/11/2018-09-07-Full-Circle.png", ""))
		))
	}

	private val filter = BusinessCatComicFilter()
	private val htmlState: HtmlState = ResourceLoader
			.loadDocument(javaClass, "business-cat.html", "https://www.businesscatcomic.com/")
			.let { HtmlState("https://www.businesscatcomic.com/", it) }

}
