package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.states.ComicState.Comic
import net.pterodactylus.rhynodge.states.ComicState.Strip

class AdventuresWithEggieComicSiteFilterTest : ComicSiteFilterTest() {

	override val filter = AdventuresWithEggieComicSiteFilter()
	override val baseUrl = "https://adventureswitheggie.com/"
	override val resource = "adventures-with-eggie.html"
	override val expectedComics = listOf(
		Comic("Chapter 2 Page 22")
			.add(Strip("https://i0.wp.com/AdventuresWithEggie.com/wp-content/uploads/2022/08/AWE-Chap2Page22Site-scaled.jpg?resize=819%2C2560", ""))
	)

}
