package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.filters.ResourceLoader.loadDocument
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

/**
 * Unit test for [WetterComFilter].
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComFilterTest {

    @Rule @JvmField val expectedException = ExpectedException.none()

    private val url = "http://www.wetter.com/wetter_aktuell/wettervorhersage/heute/deutschland/hamburg/DE0004130.html"
    private val filter = WetterComFilter()

    @Test
    fun filterReturnsFailedStateWhenGivenFailedState() {
        val exception = mock(Exception::class.java)
        val failedState = FailedState(exception)
        val newState = filter.filter(failedState)
        assertThat(newState.success(), `is`(false))
        assertThat(newState.exception(), `is`(exception as Throwable))
    }

    @Test
    fun filterThrowsWhenASuccessfulNonHtmlStateIsGiven() {
        val state = mock(State::class.java)
        `when`(state.success()).thenReturn(true)
        expectedException.expect(IllegalArgumentException::class.java)
        filter.filter(state)
    }

    @Test
    fun filterParsesHtmlCorrectly() {
        val document = loadDocument(javaClass, "wetter.com.html", url)
        val htmlState = HtmlState(url, document)
        val newState = filter.filter(htmlState) as WetterComState
        assertThat(newState.dateTime, `is`(LocalDateTime.of(2016, Month.MAY, 23, 5, 0).atZone(ZoneId.of("Europe/Berlin"))))
        assertThat(newState.hours, contains(
                HourState(0, 15.0, 0.65, 0.8, WindDirection.NORTH, 5.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(1, 15.0, 0.7, 0.9, WindDirection.NONE, 5.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(2, 17.0, 0.75, 1.0, WindDirection.NORTHWEST, 5.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(3, 17.0, 0.85, 0.3, WindDirection.NORTHWEST, 5.0, "leichter Regen", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_61_S.png?201605201518"),
                HourState(4, 19.0, 0.9, 0.3, WindDirection.SOUTHWEST, 5.0, "leichter Regen", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_61_S.png?201605201518"),
                HourState(5, 20.0, 0.85, 0.3, WindDirection.SOUTHWEST, 7.0, "leichter Regen", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_61_S.png?201605201518"),
                HourState(6, 20.0, 0.75, 0.3, WindDirection.SOUTHWEST, 11.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(7, 20.0, 0.70, 0.3, WindDirection.WEST, 11.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(8, 20.0, 0.70, 0.2, WindDirection.WEST, 9.0, "leichter Regen-schauer", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_80_S.png?201605201518"),
                HourState(9, 20.0, 0.70, 0.4, WindDirection.WEST, 5.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(10, 20.0, 0.70, 0.4, WindDirection.WEST, 7.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(11, 19.0, 0.70, 0.4, WindDirection.WEST, 11.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(12, 18.0, 0.70, 0.4, WindDirection.NORTHWEST, 12.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(13, 17.0, 0.70, 0.4, WindDirection.NORTHWEST, 11.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(14, 16.0, 0.75, 0.4, WindDirection.NORTHWEST, 12.0, "Regenschauer", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/d_81_S.png?201605201518"),
                HourState(15, 15.0, 0.85, 0.2, WindDirection.WEST, 12.0, "leichter Regen", "http://ls1.wettercomassets.com/wcomv5/images/icons/small/d_61_S.png?201605201518"),
                HourState(16, 14.0, 0.9, 0.2, WindDirection.WEST, 14.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(17, 14.0, 0.9, 0.2, WindDirection.NORTHWEST, 12.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(18, 14.0, 0.9, 0.2, WindDirection.WEST, 12.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(19, 13.0, 0.85, 0.1, WindDirection.NORTHWEST, 11.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(20, 13.0, 0.8, 0.01, WindDirection.WEST, 11.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(21, 12.0, 0.75, 0.2, WindDirection.WEST, 12.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(22, 12.0, 0.75, 0.2, WindDirection.NORTHWEST, 12.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518"),
                HourState(23, 11.0, 0.75, 0.2, WindDirection.NORTHWEST, 12.0, "leichter Regen", "http://ls2.wettercomassets.com/wcomv5/images/icons/small/n_61_S.png?201605201518")
        ))
    }

}
