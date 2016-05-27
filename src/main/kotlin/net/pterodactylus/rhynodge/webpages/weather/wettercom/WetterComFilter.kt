package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.Filter
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.HtmlState
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.time.ZoneId.of
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * HTML parser for [wetter.com](https://www.wetter.com/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComFilter : Filter {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    override fun filter(state: State?): State {
        if (state?.success()?.not() ?: true) {
            return FailedState.from(state)
        }
        if (state !is HtmlState) {
            throw IllegalArgumentException("")
        }

        return parseWetterComState(state)
    }

    private fun parseWetterComState(state: HtmlState): State {
        val dateTime = parseDateTime(state.document()) ?: return FailedState(IllegalArgumentException("no date present"))
        val wetterComState = WetterComState(dateTime)
        parseHourStates(state.document()).forEach { wetterComState.addHour(it) }
        return wetterComState
    }

    private fun parseDateTime(document: Document): ZonedDateTime? {
        val dateElement = document.select("#furtherDetails .portable-mb h3")
                .single()?.text()?.split(",")?.get(1)?.trim() ?: return null
        val timeElement = document.select(".weather-strip--detail .delta.palm-hide")
                .first()?.text()?.split(" ")?.first() ?: return null
        return LocalDateTime.from(dateTimeFormatter.parse("%s %s".format(dateElement, timeElement))).atZone(of("Europe/Berlin"))
    }

    private fun parseHourStates(document: Document): List<HourState> {
        return document.select(".weather-strip--detail").mapIndexed { index, element -> parseHourState(index, element) }
    }

    private fun parseHourState(index: Int, hourElement: Element): HourState {
        return HourState.atHour(index)
                .temperature(parseTemperature(hourElement))
                .rainProbability(parseRainProbability(hourElement))
                .rainAmount(parseRainAmount(hourElement))
                .windFrom(parseWindDirection(hourElement))
                .at(parseWindSpeed(hourElement))
                .describedAs(parseDescription(hourElement))
                .withImage(parseImageUrl(hourElement))
                .build()
    }

    private fun parseTemperature(hourElement: Element) =
            hourElement.extractText(".weather-strip__2 .item-weathericon .palm-hide").filter { (it >= '0') and (it <= '9') }.toDouble()

    private fun parseRainProbability(hourElement: Element) =
            hourElement.extractText(".weather-strip__3 .text--left:eq(0) .flag__body span:eq(0)").filter { (it >= '0') and (it <= '9') }.toDouble() / 100.0

    private fun parseRainAmount(hourElement: Element) =
            hourElement.extractText(".weather-strip__3 .text--left:eq(0) .flag__body span:eq(1)").trim().split(" ")[1].toDouble()

    private fun parseWindDirection(hourElement: Element) =
            hourElement.extractText(".weather-strip__3 .text--left:eq(1) .flag__body span:eq(0)").trim().split(",")[0].toWindDirection()

    private fun parseWindSpeed(hourElement: Element) =
            hourElement.extractText(".weather-strip__3 .text--left:eq(1) .flag__body span:eq(0)").split(Regex("[, ]+"))[1].toDouble()

    private fun parseDescription(hourElement: Element) =
            hourElement.extractText(".weather-strip__1 .vhs-text--small")

    private fun parseImageUrl(hourElement: Element) =
            hourElement.select(".weather-strip__2 img").firstOrNull()?.attr("src") ?: ""

    private fun Element.extractText(selector: String) = select(selector).firstOrNull()?.text() ?: ""

}
