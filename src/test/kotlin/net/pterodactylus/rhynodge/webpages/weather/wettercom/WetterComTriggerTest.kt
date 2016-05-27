package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.Reaction
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.mockito.Mockito
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Unit test for [WetterComTrigger].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComTriggerTest {

    private val trigger = WetterComTrigger()
    private val now = ZonedDateTime.now()
    private val previousState = WetterComState(now)

    @Test
    fun equalStatesAreNotMerged() {
        val currentState = WetterComState(now)
        val newState = trigger.mergeStates(previousState, currentState) as WetterComState
        assertThat(newState, `is`(previousState))
        assertThat(newState, `is`(currentState))
    }

    @Test
    fun currentStateIsReturnedIfDifferentFromPreviousState() {
        val currentState = WetterComState(now.minusDays(1))
        val newState = trigger.mergeStates(previousState, currentState) as WetterComState
        assertThat(newState, `is`(currentState))
    }

    @Test
    fun mergingEqualStatesDoesNotTrigger() {
        val currentState = WetterComState(now)
        trigger.mergeStates(previousState, currentState) as WetterComState
        assertThat(trigger.triggers(), `is`(false))
    }

    @Test
    fun mergingDifferentStatesDoesTrigger() {
        val currentState = WetterComState(now.minusDays(1))
        trigger.mergeStates(previousState, currentState) as WetterComState
        assertThat(trigger.triggers(), `is`(true))
    }

    @Test
    fun outputSummaryContainsCorrectDate() {
        val currentState = WetterComState(ZonedDateTime.of(2000, 1, 1, 5, 0, 0, 0, ZoneId.of("Europe/Berlin")))
        trigger.mergeStates(previousState, currentState) as WetterComState
        val reaction = Mockito.mock(Reaction::class.java)
        val output = trigger.output(reaction)
        assertThat(output.summary(), `is`("The weather (according to wetter.com) on January 1, 2000"))
    }

    @Test
    fun outputHtmlContainsHourStates() {
        val currentState = WetterComState(now.minusDays(1))
        currentState.addHour(HourState(0, 10.0, 0.11, 12.0, WindDirection.NORTH, 13.0, "Rain", "http://1"))
        currentState.addHour(HourState(1, 14.0, 0.15, 16.0, WindDirection.SOUTH, 17.0, "Sun", "http://2"))
        trigger.mergeStates(previousState, currentState) as WetterComState
        val reaction = Mockito.mock(Reaction::class.java)
        val output = trigger.output(reaction)
        val htmlText = output.text("text/html", -1)
        assertThat(htmlText, containsString("10 °C"))
        assertThat(htmlText, containsString("11%"))
        assertThat(htmlText, containsString("12 l/m"))
        assertThat(htmlText, containsString("13 km/h"))
        assertThat(htmlText, containsString("Rain"))
        assertThat(htmlText, containsString("http://1"))
        assertThat(htmlText, containsString("14 °C"))
        assertThat(htmlText, containsString("15%"))
        assertThat(htmlText, containsString("16 l/m"))
        assertThat(htmlText, containsString("17 km/h"))
        assertThat(htmlText, containsString("Sun"))
        assertThat(htmlText, containsString("http://2"))
    }

}
