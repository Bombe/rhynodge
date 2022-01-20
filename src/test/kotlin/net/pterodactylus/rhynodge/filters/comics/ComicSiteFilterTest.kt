package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ComicSiteFilter
import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

abstract class ComicSiteFilterTest {

	protected abstract val filter: ComicSiteFilter
	protected abstract val baseUrl: String
	protected abstract val resource: String
	protected abstract val expectedComics: List<Comic>

	private val htmlState: HtmlState by lazy {
		ResourceLoader
			.loadDocument(javaClass, resource, baseUrl)
			.let { HtmlState(baseUrl, it) }
	}

	@Test
	fun `comic is loaded correctly`() {
		val comicState = filter.filter(htmlState) as ComicState
		assertThat(comicState.comics(), equalTo(expectedComics))
	}

}
