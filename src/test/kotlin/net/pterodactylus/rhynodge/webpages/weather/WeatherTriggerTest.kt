package net.pterodactylus.rhynodge.webpages.weather

import net.pterodactylus.rhynodge.State
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.ZoneId.of
import java.time.ZonedDateTime

/**
 * Unit test for [WeatherTrigger].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WeatherTriggerTest {

    private val now = ZonedDateTime.now(of("Europe/Berlin"))
    private val previousState = WeatherState("Weather", now.minusDays(1))
    private val trigger = WeatherTrigger()

    @Test
    fun currentStateIsAlwaysReturned() {
        val currentState = WeatherState("Weather", now)
        assertThat(trigger.mergeStates(previousState, currentState), `is`(currentState as State))
    }

    @Test
    fun triggerDoesNotTriggerIfStateHasNotChanged() {
        val currentState = WeatherState("Weather", now.minusDays(1))
        trigger.mergeStates(previousState, currentState)
        assertThat(trigger.triggers(), `is`(false))
    }

    @Test
    fun triggerDoesTriggerIfStateHasChanged() {
        val currentState = WeatherState("Weather", now)
        trigger.mergeStates(previousState, currentState)
        assertThat(trigger.triggers(), `is`(true))
    }

}
