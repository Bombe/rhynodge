package net.pterodactylus.rhynodge.triggers

import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.StateManagerTest.TestState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.sameInstance
import org.junit.Test

class AlwaysTriggerTest {

	@Test
	fun `merging states returns the current state`() {
		assertThat(trigger.mergeStates(previousState, successfulState), sameInstance(successfulState))
	}

	@Test
	fun `successful state triggers`() {
		trigger.mergeStates(previousState, successfulState)
		assertThat(trigger.triggers(), equalTo(true))
	}

	@Test
	fun `failed state also triggers`() {
		trigger.mergeStates(previousState, failedState)
		assertThat(trigger.triggers(), equalTo(true))
	}

	private val trigger = AlwaysTrigger()
	private val previousState = TestState()
	private val successfulState: State = TestState()
	private val failedState: State = FailedState()

}
