package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.Filter
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.HtmlState
import net.pterodactylus.rhynodge.webpages.weather.HourState
import net.pterodactylus.rhynodge.webpages.weather.WeatherState
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import net.pterodactylus.rhynodge.webpages.weather.toWindDirection
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

    override fun filter(state: State): State {
        if (state.success().not()) {
            return FailedState.from(state)
        }
        if (state !is HtmlState) {
            throw IllegalArgumentException("")
        }

        return parseWetterComState(state)
    }

    private fun parseWetterComState(state: HtmlState): State {
        val dateTime = parseDateTime(state.document()) ?: return FailedState(IllegalArgumentException("no date present"))
        val wetterComState = WeatherState("wetter.com", dateTime)
        parseHourStates(state.document()).forEach { wetterComState += it }
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
        return HourState(
                hourIndex = index,
                temperature = hourElement.temperature,
                rainProbability = hourElement.rainProbability,
                rainAmount = hourElement.rainAmount,
                windDirection = hourElement.windDirection,
                windSpeed = hourElement.windSpeed,
                description = hourElement.description,
                image = hourElement.imageUrl
        )
    }

    private val Element.temperature: Int
        get() = extractText(".weather-strip__2 .item-weathericon .palm-hide").filter { (it >= '0') and (it <= '9') }.toInt()

    private val Element.rainProbability: Double
        get() = extractText(".weather-strip__3 .text--left:eq(0) .flag__body span:eq(0)").filter { (it >= '0') and (it <= '9') }.toDouble() / 100.0

    private val Element.rainAmount: Double
        get() = extractText(".weather-strip__3 .text--left:eq(0) .flag__body span:eq(1)").trim().split(" ").getOrNull(1)?.toDouble() ?: 0.0

    private val Element.windDirection: WindDirection
        get() = extractText(".weather-strip__3 .text--left:eq(1) .flag__body span:eq(0)").trim().split(",")[0].toWindDirection()

    private val Element.windSpeed: Int
        get() = extractText(".weather-strip__3 .text--left:eq(1) .flag__body span:eq(0)").split(Regex("[, ]+"))[1].toInt()

    private val Element.description: String
        get() = extractText(".weather-strip__1 .vhs-text--small")

    private val Element.imageUrl: String
        get() = select(".weather-strip__2 img").firstOrNull()?.attr("src") ?: ""

    private fun Element.extractText(selector: String) = select(selector).firstOrNull()?.text() ?: ""

}
