package net.pterodactylus.rhynodge.queries

import net.pterodactylus.rhynodge.Query
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState

/**
 * [Query] that can run a number of other [Query]s, returning the first successful [State] that is encountered.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class FallbackQuery(private vararg val queries: Query) : Query {

	init {
		if (queries.isEmpty()) {
			throw IllegalArgumentException("queries must not be empty")
		}
	}

	override fun state(): State {
		var lastFailedState: State = FailedState()
		queries.asSequence().map(Query::state).forEach { lastFailedState = it; if (it.success()) return it }
		return lastFailedState
	}

}
