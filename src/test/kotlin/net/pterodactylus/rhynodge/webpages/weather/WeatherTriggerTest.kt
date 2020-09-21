package net.pterodactylus.rhynodge.webpages.weather

import net.pterodactylus.rhynodge.Reaction
import net.pterodactylus.rhynodge.State
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File
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

    @Test
    fun outputContainsCorrectSummary() {
        val currentState = WeatherState("Weather", ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        assertThat(output.summary(), `is`("The Weather (according to Weather) on May 28, 2016"))
    }

    @Test
    fun outputContainsCorrectHourData() {
        val currentState = WeatherState("Weather", ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        currentState += HourState(0, 10, 11, 0.12, 13.0, WindDirection.SOUTHSOUTHEAST, 14, 15, 0.16, "17", "http://18")
        currentState += HourState(1, 20, 21, 0.22, 23.0, WindDirection.NORTHNORTHWEST, 24, 25, 0.26, "27", "http://28")
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        val htmlText = output.text("text/html")
        File("/tmp/wetter.html").writer().use { it.write(htmlText) }
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

    @Test
    fun outputContainsColumnHeadersForAllColumns() {
        val currentState = WeatherState("Weather", ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        currentState += HourState(0, 10, 11, 0.12, 13.0, WindDirection.SOUTHSOUTHEAST, 14, 15, 0.16, "17", "http://18")
        currentState += HourState(1, 20, 21, 0.22, 23.0, WindDirection.NORTHNORTHWEST, 24, 25, 0.26, "27", "http://28")
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        val htmlText = output.text("text/html")
        assertThat(htmlText, containsString("Time"))
        assertThat(htmlText, containsString("Temperature"))
        assertThat(htmlText, containsString("feels like"))
        assertThat(htmlText, containsString("Chance of Rain"))
        assertThat(htmlText, containsString("Amount"))
        assertThat(htmlText, containsString("Wind from"))
        assertThat(htmlText, containsString("Speed"))
        assertThat(htmlText, containsString("Gusts"))
        assertThat(htmlText, containsString("Humidity"))
        assertThat(htmlText, containsString("Description"))
        assertThat(htmlText, containsString("Image"))
    }

    @Test
    fun outputDoesNotContainColumnHeadersForMissingColumns() {
        val currentState = WeatherState("Weather", ZonedDateTime.of(2016, 5, 28, 0, 0, 0, 0, of("Europe/Berlin")))
        currentState += HourState(0, 10, null, 0.12, 13.0, WindDirection.SOUTHSOUTHEAST, 14, null, null, "17", "http://18")
        currentState += HourState(1, 20, null, 0.22, 23.0, WindDirection.NORTHNORTHWEST, 24, null, null, "27", "http://28")
        trigger.mergeStates(previousState, currentState)
        val reaction = mock(Reaction::class.java)
        val output = trigger.output(reaction)
        val htmlText = output.text("text/html")
        assertThat(htmlText, containsString("Time"))
        assertThat(htmlText, containsString("Temperature"))
        assertThat(htmlText, not(containsString("feels like")))
        assertThat(htmlText, containsString("Chance of Rain"))
        assertThat(htmlText, containsString("Amount"))
        assertThat(htmlText, containsString("Wind from"))
        assertThat(htmlText, containsString("Speed"))
        assertThat(htmlText, not(containsString("Gusts")))
        assertThat(htmlText, not(containsString("Humidity")))
        assertThat(htmlText, containsString("Description"))
        assertThat(htmlText, containsString("Image"))
    }

}
