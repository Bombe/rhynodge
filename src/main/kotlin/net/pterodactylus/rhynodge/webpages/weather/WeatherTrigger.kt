package net.pterodactylus.rhynodge.webpages.weather

import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.Trigger

/**
 * Detects changes in the weather and creates email texts.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WeatherTrigger : Trigger {

	private lateinit var state: WeatherState
	private var changed = false

	override fun mergeStates(previousState: State, currentState: State): State {
		changed = previousState != currentState
		state = currentState as WeatherState
		return currentState
	}

	override fun triggers() = changed

}
