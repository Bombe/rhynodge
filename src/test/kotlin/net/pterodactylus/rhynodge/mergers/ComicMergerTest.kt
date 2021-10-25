package net.pterodactylus.rhynodge.mergers

import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test

class ComicMergerTest {

	private val comicMerger = ComicMerger()

	@Test
	fun `comic merger does not reorder comics`() {
		val oldComicState = ComicState(generateListOfComics())
		val newComicState = ComicState(generateListOfComics().plusElement(Comic("new 1")))
		val mergedComicState = comicMerger.mergeStates(oldComicState, newComicState) as ComicState
		assertThat(mergedComicState.comics(), contains(*generateListOfComics().plusElement(Comic("new 1")).toTypedArray()))
	}

}

private fun generateListOfComics(): List<Comic> = (1..40).map { Comic("comic $it") }
