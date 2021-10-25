package net.pterodactylus.rhynodge.mergers

import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.StateManagerTest.TestState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.sameInstance
import org.junit.Test

class LastStateMergerTest {

	@Test
	fun `merging states returns the current state`() {
		assertThat(merger.mergeStates(previousState, successfulState), sameInstance(successfulState))
	}

	private val merger = LastStateMerger()
	private val previousState = TestState()
	private val successfulState: State = TestState()

}
