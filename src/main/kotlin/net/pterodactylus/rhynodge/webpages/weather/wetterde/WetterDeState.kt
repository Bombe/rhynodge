package net.pterodactylus.rhynodge.webpages.weather.wetterde

import com.fasterxml.jackson.annotation.JsonProperty
import net.pterodactylus.rhynodge.states.AbstractState
import java.time.Instant
import java.time.ZoneId.of
import java.time.ZonedDateTime

/**
 * Contains the state parsed from [wetter.de](https://www.wetter.de/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeState(val dateTime: ZonedDateTime) : AbstractState(true), Iterable<HourState> {

    constructor(@JsonProperty("dateTime") timeMillis: Long) : this(Instant.ofEpochMilli(timeMillis).atZone(of("Europe/Berlin")))

    val hours: List<HourState> = mutableListOf()

    val timeMillis: Long
        @JsonProperty("dateTime") get() {
            return dateTime.toInstant().toEpochMilli()
        }

    override fun iterator(): Iterator<HourState> = hours.iterator()

    operator fun plusAssign(hourState: HourState) {
        (hours as MutableList).add(hourState)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is WetterDeState) {
            return false
        }
        return (dateTime == other.dateTime) and (hours == other.hours)
    }

}
