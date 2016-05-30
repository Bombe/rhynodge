package net.pterodactylus.rhynodge.webpages.weather

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.img
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.unsafe
import net.pterodactylus.rhynodge.Reaction
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.Trigger
import net.pterodactylus.rhynodge.output.DefaultOutput
import net.pterodactylus.rhynodge.output.Output
import java.text.DateFormat
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Detects changes in the weather and creates email texts.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WeatherTrigger : Trigger {

    private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)
    private lateinit var state: WeatherState
    private var changed = false

    override fun mergeStates(previousState: State, currentState: State): State {
        changed = previousState != currentState
        state = currentState as WeatherState
        return currentState
    }

    override fun triggers(): Boolean {
        return changed
    }

    override fun output(reaction: Reaction): Output {
        val output = DefaultOutput("The Weather (according to ${state.service}) on ${dateFormatter.format(state.dateTime.toInstant().toEpochMilli())}")
        output.addText("text/html", generateHtmlOutput())
        return output
    }

    private fun generateHtmlOutput(): String {
        return createHTML().html {
            head {
                style("text/css") {
                    unsafe {
                        +".weather-states { display: table; } "
                        +".hour-state, .header { display: table-row; } "
                        +".hour-state > div { display: table-cell; padding: 0em 0.5em; text-align: center; } "
                        +".header > div { display: table-cell; padding: 0em 0.5em; font-weight: bold; text-align: center; } "
                    }
                }
            }
            body {
                val startTime = state.dateTime.toInstant()
                h1 { +"The Weather (according to wetter.de) on %s".format(dateFormatter.format(startTime.toEpochMilli())) }
                val showFeltTemperature = state.any { it.feltTemperature != null }
                val showGustSpeed = state.any { it.gustSpeed != null }
                val showHumidity = state.any { it.humidity != null }
                div("weather-states") {
                    div("header") {
                        div { +"Time" }
                        div { +"Temperature" }
                        if (showHumidity) {
                            div { +"feels like" }
                        }
                        div { +"Chance of Rain" }
                        div { +"Amount" }
                        div { +"Wind from" }
                        div { +"Speed" }
                        if (showGustSpeed) {
                            div { +"Gusts" }
                        }
                        if (showHumidity) {
                            div { +"Humidity" }
                        }
                        div { +"Description" }
                        div { +"Image" }
                    }
                    state.forEach {
                        div("hour-state") {
                            div("time") { +"%tH:%<tM".format(startTime.plus(it.hourIndex.toLong(), ChronoUnit.HOURS).toEpochMilli()) }
                            div("temperature") { +"${it.temperature} °C" }
                            if (showFeltTemperature) {
                                div("felt-temperature") { +if (it.feltTemperature != null) "(${it.feltTemperature} °C)" else "" }
                            }
                            div("rain-probability") { +"${it.rainProbability.times(100).toInt()}%" }
                            div("rain-amount") { +"${it.rainAmount.minDigits()} l/m²" }
                            div("wind-direction") { +it.windDirection.arrow }
                            div("wind-speed") { +"${it.windSpeed} km/h" }
                            if (showGustSpeed) {
                                div("gust-speed") { +if (it.gustSpeed != null) "(up to ${it.gustSpeed} km/h)" else "" }
                            }
                            if (showHumidity) {
                                div("humidity") { +if (it.humidity != null) "${it.humidity.times(100).toInt()}%" else "" }
                            }
                            div("description") { +it.description }
                            div("image") { img(src = it.image) }
                        }
                    }
                }
            }
        }.toString()
    }

    private fun Double.minDigits(): String {
        return this.toString().replace(Regex("\\.0*$"), "")
    }

}
