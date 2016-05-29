package net.pterodactylus.rhynodge.webpages.weather

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import net.pterodactylus.rhynodge.states.AbstractState
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Contains a weather state.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WeatherState(val service: String, val dateTime: ZonedDateTime) : AbstractState(true), Iterable<HourState> {

    constructor(@JsonProperty("service") service: String, @JsonProperty("dateTime") time: Long) :
    this(service, Instant.ofEpochMilli(time).atZone(ZoneId.of("Europe/Berlin")))

    @JsonProperty("hours")
    val hours: List<HourState> = mutableListOf()

    val timeMillis: Long
        @JsonGetter("dateTime")
        get() = dateTime.toInstant().toEpochMilli()

    operator fun plusAssign(hourState: HourState) {
        (hours as MutableList<HourState>).add(hourState)
    }

    override fun iterator(): Iterator<HourState> {
        return hours.iterator()
    }

    override fun equals(other: Any?): Boolean {
        other as? WeatherState ?: return false
        return (dateTime == other.dateTime) and (hours == other.hours)
    }

}
