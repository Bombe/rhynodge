package net.pterodactylus.rhynodge.triggers

import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.ComicState.Comic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class NewComicTriggerTest {

	private val newComicTrigger = NewComicTrigger()

	@Test
	fun `comic trigger recognizes there are no new comic`() {
		val oldComicState = ComicState(generateListOfComics())
		val newComicState = ComicState(generateListOfComics())
		newComicTrigger.mergeStates(oldComicState, newComicState)
		assertThat(newComicTrigger.triggers(), equalTo(false))
	}

	@Test
	fun `comic trigger recognizes new comics`() {
		val oldComicState = ComicState(generateListOfComics())
		val newComicState = ComicState(generateListOfComics().plusElement(Comic("new 1")))
		newComicTrigger.mergeStates(oldComicState, newComicState)
		assertThat(newComicTrigger.triggers(), equalTo(true))
	}

	@Test
	fun `comic trigger does not reorder comics`() {
		val oldComicState = ComicState(generateListOfComics())
		val newComicState = ComicState(generateListOfComics().plusElement(Comic("new 1")))
		val mergedComicState = newComicTrigger.mergeStates(oldComicState, newComicState) as ComicState
		assertThat(mergedComicState.comics(), contains(*generateListOfComics().plusElement(Comic("new 1")).toTypedArray()))
	}

}

private fun generateListOfComics(): List<Comic> = (1..40).map { Comic("comic $it") }
