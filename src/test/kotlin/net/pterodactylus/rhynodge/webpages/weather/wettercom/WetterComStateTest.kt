package net.pterodactylus.rhynodge.webpages.weather.wettercom

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Unit test for [WetterComState].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComStateTest {

    @Test
    fun statesWithoutHoursEqualOneAnother() {
        val now = Instant.now().atZone(ZoneId.of("UTC"))
        println("%s %s".format(now, now.javaClass))
        val firstState = WetterComState(LocalDateTime.from(now))
        val secondState = WetterComState(LocalDateTime.from(now))
        assertThat(firstState, `is`(secondState))
    }

    @Test
    fun statesWithTheSameHoursAreEqual() {
        val now = Instant.now().atZone(ZoneId.of("UTC"))
        println("%s %s".format(now, now.javaClass))
        val firstState = WetterComState(LocalDateTime.from(now))
        firstState.addHour(HourState(0, 10.0, 0.05, 0.0, WindDirection.NORTH, 5.0, "Fine", "http://1"))
        firstState.addHour(HourState(1, 12.0, 0.1, 2.0, WindDirection.WEST, 8.0, "Superb", "http://2"))
        val secondState = WetterComState(LocalDateTime.from(now))
        secondState.addHour(HourState(0, 10.0, 0.05, 0.0, WindDirection.NORTH, 5.0, "Fine", "http://1"))
        secondState.addHour(HourState(1, 12.0, 0.1, 2.0, WindDirection.WEST, 8.0, "Superb", "http://2"))
        assertThat(firstState, `is`(secondState))
    }

}
