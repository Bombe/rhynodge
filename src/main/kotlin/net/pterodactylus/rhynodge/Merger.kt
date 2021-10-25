package net.pterodactylus.rhynodge

interface Merger {

	fun mergeStates(previousState: State, currentState: State): State

}
