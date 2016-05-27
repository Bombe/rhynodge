package net.pterodactylus.rhynodge.webpages.weather.wettercom

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import net.pterodactylus.rhynodge.states.AbstractState
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Contains the state parsed from [wetter.com](https://www.wetter.com/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComState(val dateTime: ZonedDateTime) : AbstractState(true), Iterable<HourState> {

    constructor(@JsonProperty("dateTime") time: Long) :
    this(Instant.ofEpochMilli(time).atZone(ZoneId.of("Europe/Berlin")))

    @JsonProperty("hours")
    val hours: List<HourState> = mutableListOf()

    val timeMillis: Long
        @JsonGetter("dateTime")
        get() = dateTime.toInstant().toEpochMilli()

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
