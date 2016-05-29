package net.pterodactylus.rhynodge.webpages.weather.wetterde

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.AbstractState
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.HtmlState
import net.pterodactylus.rhynodge.webpages.weather.HourState
import net.pterodactylus.rhynodge.webpages.weather.WeatherState
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Unit test for [WetterDeFilter].

 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeFilterTest {

    @Rule @JvmField val expectedException = ExpectedException.none()

    private val url = ""
    private val filter = WetterDeFilter()

    @Test
    fun filterReturnsFailedStateIfGivenAFailedState() {
        val previousState = FailedState.INSTANCE
        assertThat(filter.filter(previousState).success(), `is`(false))
    }

    @Test
    fun filterThrowsExceptionIfNotGivenAnHtmlState() {
        val successfulHonHtmlState = object : AbstractState(true) {}
        expectedException.expect(IllegalArgumentException::class.java)
        filter.filter(successfulHonHtmlState)
    }

    @Test
    fun filterCanParseDateCorrectly() {
        val htmlState = HtmlState(url, ResourceLoader.loadDocument(javaClass, "wetter.de.html", url))
        val wetterDeState = filter.filter(htmlState) as WeatherState
        assertThat(wetterDeState.dateTime, `is`(ZonedDateTime.of(2016, 5, 30, 0, 0, 0, 0, ZoneId.of("Europe/Berlin"))))
    }

    @Test
    fun filterParsesHoursCorrectly() {
        val htmlState = HtmlState(url, ResourceLoader.loadDocument(javaClass, "wetter.de.html", url))
        val wetterDeState = filter.filter(htmlState) as WeatherState
        assertThat(wetterDeState, contains(
                HourState(0, 18, 18, 0.47, 0.4, WindDirection.NORTHEAST, 19, 41, 0.91, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/017_M.png?o44shg"),
                HourState(1, 18, 18, 0.60, 0.6, WindDirection.NORTHEAST, 20, 39, 0.91, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/017_M.png?o44shg"),
                HourState(2, 18, 18, 0.59, 0.6, WindDirection.NORTHEAST, 20, 39, 0.89, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/017_M.png?o44shg"),
                HourState(3, 18, 18, 0.49, 0.7, WindDirection.NORTHEAST, 19, 39, 0.89, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/017_M.png?o44shg"),
                HourState(4, 18, 18, 0.38, 0.5, WindDirection.NORTHEAST, 19, 39, 0.89, "unbeständig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/022_M.png?o44shg"),
                HourState(5, 17, 18, 0.35, 0.4, WindDirection.NORTHEAST, 19, 39, 0.90, "unbeständig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/010_M.png?o44shg"),
                HourState(6, 17, 19, 0.45, 0.6, WindDirection.NORTHEAST, 19, 39, 0.91, "unbeständig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/010_M.png?o44shg"),
                HourState(7, 18, 19, 0.59, 1.0, WindDirection.NORTHEAST, 19, 39, 0.88, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(8, 19, 20, 0.64, 0.7, WindDirection.EASTNORTHEAST, 19, 39, 0.83, "unbeständig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/010_M.png?o44shg"),
                HourState(9, 20, 21, 0.59, 0.3, WindDirection.EASTNORTHEAST, 20, 41, 0.79, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(10, 21, 21, 0.50, 0.2, WindDirection.EASTNORTHEAST, 20, 43, 0.75, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(11, 22, 22, 0.39, 0.1, WindDirection.EASTNORTHEAST, 20, 44, 0.71, "wechselhaft", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/010_M.png?o44shg"),
                HourState(12, 23, 23, 0.24, 0.05, WindDirection.EASTNORTHEAST, 20, 44, 0.70, "heiter", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/003_M.png?o44shg"),
                HourState(13, 24, 24, 0.13, 0.02, WindDirection.EAST, 20, 46, 0.68, "heiter", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/003_M.png?o44shg"),
                HourState(14, 24, 24, 0.11, 0.01, WindDirection.EAST, 20, 48, 0.66, "heiter", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/003_M.png?o44shg"),
                HourState(15, 25, 23, 0.17, 0.2, WindDirection.EAST, 22, 48, 0.64, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(16, 24, 22, 0.23, 0.5, WindDirection.EAST, 22, 50, 0.65, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(17, 24, 22, 0.27, 0.7, WindDirection.EAST, 24, 50, 0.67, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(18, 24, 22, 0.34, 1.0, WindDirection.EASTNORTHEAST, 24, 48, 0.68, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(19, 23, 21, 0.45, 1.6, WindDirection.EASTNORTHEAST, 22, 48, 0.72, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(20, 22, 23, 0.50, 0.9, WindDirection.EASTNORTHEAST, 20, 44, 0.77, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(21, 21, 22, 0.44, 0.1, WindDirection.EASTNORTHEAST, 19, 41, 0.80, "gewittrig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/016_M.png?o44shg"),
                HourState(22, 20, 20, 0.36, 0.09, WindDirection.EASTNORTHEAST, 17, 39, 0.84, "heiter bis wolkig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/004_M.png?o44shg"),
                HourState(23, 19, 19, 0.31, 0.06, WindDirection.EASTNORTHEAST, 17, 35, 0.87, "heiter bis wolkig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/004_M.png?o44shg"),
                HourState(24, 19, 20, 0.22, 0.03, WindDirection.EASTNORTHEAST, 15, 33, 0.89, "heiter bis wolkig", "http://cdn.static-fra.de/wetterv3/css/images/icons/weather/m/004_M.png?o44shg")
        ))
    }

}
