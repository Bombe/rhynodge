package net.pterodactylus.rhynodge.webpages.weather.wetterde

import net.pterodactylus.rhynodge.Filter
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.HtmlState
import net.pterodactylus.rhynodge.webpages.weather.HourState
import net.pterodactylus.rhynodge.webpages.weather.WindDirection
import net.pterodactylus.rhynodge.webpages.weather.toWindDirection
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * HTML parser for [wetter.de](https://www.wetter.de/).
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeFilter : Filter {

    object DateParser {
        val parser = DateTimeFormatter.ofPattern("dd. MMM yyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("Europe/Berlin"))
    }

    override fun filter(state: State): State {
        if (state.success().not()) {
            return FailedState.from(state)
        }
        state as? HtmlState ?: throw IllegalArgumentException("state is not an HTML state")

        return parseWetterDeState(state)
    }

    private fun parseWetterDeState(htmlState: HtmlState): WetterDeState {
        val dateTime = parseDate(htmlState.document()) ?: throw IllegalArgumentException("date can not be parsed")
        val wetterDeState = WetterDeState(dateTime)
        parseHourStates(htmlState.document()).forEach { wetterDeState += it }
        return wetterDeState
    }

    private fun parseDate(document: Document): ZonedDateTime? {
        return document.select(".forecast-detail-headline").text()
                .extract(Regex(".*?([0-9]{1,2}\\. [^ ]+ [0-9]{4}).*?$"))?.toDate() ?: return null
    }

    private fun String.extract(regex: Regex): String? {
        return regex.find(this)?.groups?.get(1)?.value
    }

    private fun String.toDate(): ZonedDateTime? {
        return try {
            ZonedDateTime.of(DateParser.parser.parse(this, LocalDate::from), LocalTime.of(0, 0), ZoneId.of("Europe/Berlin"))
        } catch (e: DateTimeException) {
            e.printStackTrace()
            null
        }
    }

    private fun parseHourStates(document: Document): Collection<HourState> {
        return document.select(".forecast-detail-column-1h").mapIndexed { index, element -> parseHourState(index, element) }
    }

    private fun parseHourState(index: Int, element: Element): HourState {
        return HourState(
                index,
                element.select("span.temperature").text().extract(Regex("([0-9]+).*$"))?.toInt() ?: 0,
                element.select("span.temperature-wind-chill").text().extract(Regex("[^0-9]*?([0-9]+)[^0-9]*?"))?.toInt() ?: 0,
                (element.select(".forecast-icon-rain-fill").attr("style").extract(Regex("[^0-9]*?([0-9]+)[^0-9]*?"))?.toInt() ?: 0) / 100.0,
                element.select(".forecast-rain span:eq(4)").text().replace(',', '.').extract(Regex("[^0-9]*?([0-9\\.]+)[^0-9]*?"))?.toDouble() ?: 0.0,
                element.select(".forecast-wind-text").text().extract(Regex(".*?(Nord((nord)?(ost|west))?|Ost((nord|süd)ost)?|Süd((süd)?(ost|west))?|West((nord|süd)west)?).*?"))?.toWindDirection() ?: WindDirection.NONE,
                element.select(".forecast-wind-text span:eq(1)").text().extract(Regex(".*?([0-9]+) km/h.*?"))?.toInt() ?: 0,
                element.select(".forecast-wind-text span:eq(4)").text().extract(Regex(".*?([0-9]+) km/h.*?"))?.toInt() ?: 0,
                (element.select(".forecast-humidity-text span:eq(0)").text().extract(Regex("([0-9]+)[^0-9]*?"))?.toInt() ?: 0) / 100.0,
                element.select("span.temperature-condition").text(),
                element.select(".forecast-image img").attr("src").prefixProtocol()
        )
    }

    private fun String.prefixProtocol(): String {
        return if (startsWith("//")) "http:" + this else this
    }

}
