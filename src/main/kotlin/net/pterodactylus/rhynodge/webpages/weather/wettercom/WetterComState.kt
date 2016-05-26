package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.states.AbstractState
import java.time.LocalDateTime

/**
 * Contains the state parsed from [wetter.com](https://www.wetter.com/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComState(val dateTime: LocalDateTime) : AbstractState(true) {

    val hours: List<HourState> = mutableListOf()

    fun addHour(hourState: HourState) {
        (hours as MutableList<HourState>).add(hourState)
    }

}
