package net.pterodactylus.rhynodge.webpages.weather.wetterde

import net.pterodactylus.rhynodge.Reaction
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.mockito.Mockito.mock
import java.time.ZoneId.of
import java.time.ZonedDateTime

/**
 * Unit test for [WetterDeTriggerTest].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeTriggerTest {

    private val now = ZonedDateTime.now(of("Europe/Berlin"))
    private val previousState = WetterDeState(now.minusDays(1))
    private val trigger = WetterDeTrigger()

    @Test
    fun currentStateIsAlwaysReturned() {
        val currentState = WetterDeState(now)
        assertThat(trigger.mergeStates(previousState, currentState), `is`(currentState as State))
    }

    @Test
    fun triggerDoesNotTriggerIfStateHasNotChanged() {
        val currentState = WetterDeState(now.minusDays(1))
        trigger.mergeStates(previousState, currentState)
        assertThat(trigger.triggers(), `is`(false))
    }

    @Test
    fun triggerDoesTriggerIfStateHasChanged() {
        val currentState = WetterDeState(now)
        trigger.mergeStates(previousState, currentState)
        assertThat(trigger.triggers(), `is`(true))
    }

    @Test
    fun outputContainsCorrectSummary() {
        val currentState = WetterDeState(ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        assertThat(output.summary(), `is`("The Weather (according to wetter.de) on May 28, 2016"))
    }

    @Test
    fun outputContainsCorrectHourData() {
        val currentState = WetterDeState(ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        currentState += HourState(0, 10, 11, 0.12, 13.0, WindDirection.SOUTHSOUTHEAST, 14, 15, 0.16, "17", "http://18")
        currentState += HourState(1, 20, 21, 0.22, 23.0, WindDirection.NORTHNORTHWEST, 24, 25, 0.26, "27", "http://28")
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        val htmlText = output.text("text/html", -1)
        assertThat(htmlText, containsString("00:00"))
        assertThat(htmlText, containsString("10 °C"))
        assertThat(htmlText, containsString("(11 °C)"))
        assertThat(htmlText, containsString("12%"))
        assertThat(htmlText, containsString("13 l/m²"))
        assertThat(htmlText, containsString("↖↑"))
        assertThat(htmlText, containsString("14 km/h"))
        assertThat(htmlText, containsString("15 km/h"))
        assertThat(htmlText, containsString("16%"))
        assertThat(htmlText, containsString("17"))
        assertThat(htmlText, containsString("http://18"))
        assertThat(htmlText, containsString("01:00"))
        assertThat(htmlText, containsString("20 °C"))
        assertThat(htmlText, containsString("(21 °C)"))
        assertThat(htmlText, containsString("22%"))
        assertThat(htmlText, containsString("23 l/m²"))
        assertThat(htmlText, containsString("↘↓"))
        assertThat(htmlText, containsString("24 km/h"))
        assertThat(htmlText, containsString("25 km/h"))
        assertThat(htmlText, containsString("26%"))
        assertThat(htmlText, containsString("27"))
        assertThat(htmlText, containsString("http://28"))
    }

}
