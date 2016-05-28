package net.pterodactylus.rhynodge.webpages.weather.wetterde

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
 * TODO
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeTrigger : Trigger {

    private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)
    private lateinit var state: WetterDeState
    private var changed = false

    override fun mergeStates(previousState: State, currentState: State): State {
        changed = previousState != currentState
        state = currentState as WetterDeState
        return currentState
    }

    override fun triggers(): Boolean {
        return changed
    }

    override fun output(reaction: Reaction): Output {
        val output = DefaultOutput("The Weather (according to wetter.de) on %s".format(dateFormatter.format(state.dateTime.toInstant().toEpochMilli())))
        output.addText("text/html", generateHtmlOutput())
        return output
    }

    private fun generateHtmlOutput(): String {
        return createHTML().html {
            head {
                style("text/css") {
                    unsafe {
                        +".hour-state { display: table-row; } "
                        +".hour-state > div { display: table-cell; padding-right: 1em; } "
                    }
                }
            }
            body {
                val startTime = state.dateTime.toInstant()
                h1 { +"The Weather (according to wetter.de) on %s".format(dateFormatter.format(startTime.toEpochMilli())) }
                state.forEach {
                    div("hour-state") {
                        div("time") { +"%tH:%<tM".format(startTime.plus(it.hourIndex.toLong(), ChronoUnit.HOURS).toEpochMilli()) }
                        div("temperature") { +"%d °C".format(it.temperature) }
                        div("felt-temperature") { +"(%d °C)".format(it.feltTemperature) }
                        div("rain-probability") { +"%d%%".format((it.rainProbability * 100).toInt()) }
                        div("rain-amount") { +"%s l/m²".format(it.rainAmount.minDigits()) }
                        div("wind-direction") { +it.windDirection.arrow }
                        div("wind-speed") { +"%d km/h".format(it.windSpeed) }
                        div("gust-speed") { +"(up to %d km/h)".format(it.gustSpeed) }
                        div("humidity") { +"%d%%".format((it.humidity * 100).toInt()) }
                        div("description") { +it.description }
                        div("image") { img(src = it.image) }
                    }
                }
            }
        }.toString()
    }

    private fun Double.minDigits(): String {
        return this.toString().replace(Regex("\\.0*$"), "")
    }

}
