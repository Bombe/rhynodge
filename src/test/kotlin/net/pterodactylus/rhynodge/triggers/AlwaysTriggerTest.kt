package net.pterodactylus.rhynodge.triggers

import net.pterodactylus.rhynodge.Reaction
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.StateManagerTest.TestState
import net.pterodactylus.rhynodge.testAction
import net.pterodactylus.rhynodge.testQuery
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

	@Test
	@Suppress("NonAsciiCharacters")
	fun `output returns “true” for plain text`() {
		trigger.mergeStates(previousState, successfulState)
		val output = trigger.output(Reaction("Test", testQuery(), trigger, testAction()))
		assertThat(output.text("text/plain"), equalTo("true"))
	}

	@Test
	fun `output returns true in a div for html`() {
		trigger.mergeStates(previousState, successfulState)
		val output = trigger.output(Reaction("Test", testQuery(), trigger, testAction()))
		assertThat(output.text("text/html"), equalTo("<div>true</div>"))
	}

	private val trigger = AlwaysTrigger()
	private val previousState = TestState()
	private val successfulState: State = TestState()
	private val failedState: State = FailedState()

}
