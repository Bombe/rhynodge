package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic

class OglafComicSiteFilterTest : ComicSiteFilterTest() {

	override val filter = OglafComicSiteFilter()
	override val baseUrl = "https://www.oglaf.com/"
	override val resource = "oglaf.html"
	override val expectedComics = listOf(
		Comic("Lightning Rod")
			.add(ComicState.Strip("https://media.oglaf.com/comic/lightning_rod.jpg", "With Zeus, ‘aftercare’ usually means turning you into an animal"))
	)

}

