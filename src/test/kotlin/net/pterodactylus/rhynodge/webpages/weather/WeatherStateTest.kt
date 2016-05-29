package net.pterodactylus.rhynodge.webpages.weather

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Unit test for [WeatherState].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
const val SERVICE_NAME = "Weather Service"

class WeatherStateTest {

    private val now = Instant.now().atZone(ZoneId.of("Europe/Berlin"))

    @Test
    fun stateRetainsServiceName() {
        val state = WeatherState(SERVICE_NAME, now)
        assertThat(state.service, `is`(SERVICE_NAME))
    }

    @Test
    fun statesWithoutHoursEqualOneAnother() {
        val now = now
        val firstState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        val secondState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        assertThat(firstState, `is`(secondState))
    }

    @Test
    fun statesWithTheSameHoursAreEqual() {
        val now = now
        val firstState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        firstState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        firstState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        val secondState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        secondState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        secondState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        assertThat(firstState, `is`(secondState))
    }

    @Test
    fun iteratingDeliversHourStates() {
        val now = now
        val firstState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        firstState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        firstState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        assertThat(firstState.iterator().asSequence().toList(), `is`(firstState.hours as Iterable<HourState>))
    }

    @Test
    fun stateIsSerializableAsJson() {
        val objectMapper = ObjectMapper()
        val now = now
        val originalState = WeatherState(SERVICE_NAME, ZonedDateTime.from(now))
        originalState += HourState(0, 10, null, 0.05, 0.0, WindDirection.NORTH, 5, null, null, "Fine", "http://1")
        originalState += HourState(1, 12, null, 0.1, 2.0, WindDirection.WEST, 8, null, null, "Superb", "http://2")
        val json = objectMapper.writeValueAsString(originalState)
        println(json)
        val parsedState = objectMapper.readValue(json, WeatherState::class.java)
        assertThat(parsedState, `is`(originalState))
    }

}
