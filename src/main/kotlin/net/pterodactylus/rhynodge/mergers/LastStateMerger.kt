package net.pterodactylus.rhynodge.mergers

import net.pterodactylus.rhynodge.Merger
import net.pterodactylus.rhynodge.State

/**
 * [Merger] implementation that always returns the current state.
 */
class LastStateMerger : Merger {

	override fun mergeStates(previousState: State, currentState: State) = currentState

}
