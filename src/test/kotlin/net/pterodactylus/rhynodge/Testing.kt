package net.pterodactylus.rhynodge

import net.pterodactylus.rhynodge.output.Output
import net.pterodactylus.rhynodge.states.StateManagerTest.TestState

fun testQuery(state: State = TestState()): Query = TestQuery(state)
fun testAction(): Action = TestAction()

class TestQuery(private val state: State) : Query {
	override fun state(): State = state
}

class TestAction : Action {
	override fun execute(output: Output?) = Unit
}
