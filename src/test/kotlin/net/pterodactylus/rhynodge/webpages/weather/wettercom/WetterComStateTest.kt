package net.pterodactylus.rhynodge.webpages.weather.wettercom

import com.fasterxml.jackson.databind.ObjectMapper
import net.pterodactylus.rhynodge.webpages.weather.HourState
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Unit test for [WetterComState].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComStateTest {

    @Test
    fun statesWithoutHoursEqualOneAnother() {
        val now = Instant.now().atZone(ZoneId.of("Europe/Berlin"))
        val firstState = WetterComState(ZonedDateTime.from(now))
        val secondState = WetterComState(ZonedDateTime.from(now))
        assertThat(firstState, `is`(secondState))
    }

    @Test
    fun statesWithTheSameHoursAreEqual() {
        val now = Instant.now().atZone(ZoneId.of("Europe/Berlin"))
        val firstState = WetterComState(ZonedDateTime.from(now))
        firstState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        firstState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        val secondState = WetterComState(ZonedDateTime.from(now))
        secondState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        secondState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        assertThat(firstState, `is`(secondState))
    }

    @Test
    fun iteratingDeliversHourStates() {
        val now = Instant.now().atZone(ZoneId.of("Europe/Berlin"))
        val firstState = WetterComState(ZonedDateTime.from(now))
        firstState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        firstState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        assertThat(firstState.iterator().asSequence().toList(), `is`(firstState.hours as Iterable<HourState>))
    }

    @Test
    fun stateIsSerializableAsJson() {
        val objectMapper = ObjectMapper()
        val now = Instant.now().atZone(ZoneId.of("Europe/Berlin"))
        val originalState = WetterComState(ZonedDateTime.from(now))
        originalState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        originalState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        val json = objectMapper.writeValueAsString(originalState)
        println(json)
        val parsedState = objectMapper.readValue(json, WetterComState::class.java)
        assertThat(parsedState, `is`(originalState))
    }

}
