package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.states.AbstractState
import java.time.ZonedDateTime

/**
 * Contains the state parsed from [wetter.com](https://www.wetter.com/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComState(val dateTime: ZonedDateTime) : AbstractState(true), Iterable<HourState> {

    val hours: List<HourState> = mutableListOf()

    fun addHour(hourState: HourState) {
        (hours as MutableList<HourState>).add(hourState)
    }

    override fun iterator(): Iterator<HourState> {
        return hours.iterator()
    }

    override fun equals(other: Any?): Boolean {
        other as? WetterComState ?: return false
        return (dateTime == other.dateTime) and (hours == other.hours)
    }

}
