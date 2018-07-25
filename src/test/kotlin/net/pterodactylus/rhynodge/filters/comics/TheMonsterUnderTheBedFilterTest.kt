package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader.*
import net.pterodactylus.rhynodge.states.*
import net.pterodactylus.rhynodge.states.ComicState.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.*

class TheMonsterUnderTheBedFilterTest {

	private val filter = TheMonsterUnderTheBedFilter()
	private val htmlState = loadDocument(TheMonsterUnderTheBedFilter::class.java, "the-monster-under-the-bed.html", "http://themonsterunderthebed.net/")
			.let { HtmlState("http://themonsterunderthebed.net/", it) }

	@Test
	fun `filter can parse comic from html`() {
		val state = filter.filter(htmlState) as ComicState
		assertThat(state.comics(), contains(
				Comic("#153 “Push”")
						.add(Strip("http://themonsterunderthebed.net/wp-content/uploads/2018/07/153-Push.png", ""))
		))
	}

}
