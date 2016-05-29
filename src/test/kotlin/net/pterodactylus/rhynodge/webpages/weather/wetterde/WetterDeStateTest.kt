package net.pterodactylus.rhynodge.webpages.weather.wetterde

import com.fasterxml.jackson.databind.ObjectMapper
import net.pterodactylus.rhynodge.webpages.weather.HourState
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.ZoneId.of
import java.time.ZonedDateTime

/**
 * Unit test for [WetterDeState].
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeStateTest {

    private val now = ZonedDateTime.now(of("Europe/Berlin"))

    @Test
    fun statesWithTheSameDateAndHoursAreEqual() {
        val firstState = WetterDeState(now)
        firstState += createHourState(0)
        firstState += createHourState(1)
        val secondState = WetterDeState(now)
        secondState += createHourState(0)
        secondState += createHourState(1)
        assertThat(firstState, `is`(secondState))
    }

    private fun createHourState(hourIndex: Int): HourState {
        return HourState(
                hourIndex,
                10 + hourIndex,
                12 + hourIndex,
                (50 + hourIndex) / 100.0,
                hourIndex.toDouble(),
                WindDirection.values().get(hourIndex % WindDirection.values().size),
                20 + hourIndex,
                30 + hourIndex,
                (40 + hourIndex) / 100.0,
                "foo: " + hourIndex,
                "//" + hourIndex
        )
    }

    @Test
    fun stateIsSerializableAsJson() {
        val originalState = WetterDeState(now)
        originalState += createHourState(0)
        originalState += createHourState(1)
        val json = ObjectMapper().writeValueAsString(originalState)
        val parsedState = ObjectMapper().readValue(json, WetterDeState::class.java)
        assertThat(parsedState, `is`(originalState))
    }

}
