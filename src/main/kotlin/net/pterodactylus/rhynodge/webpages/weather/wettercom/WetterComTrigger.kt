package net.pterodactylus.rhynodge.webpages.weather.wettercom

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
import java.time.temporal.ChronoUnit.HOURS
import java.util.Locale

/**
 * TODO
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComTrigger : Trigger {

    private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)
    private var newState = false
    private lateinit var currentState: WetterComState

    override fun mergeStates(previousState: State, currentState: State): State? {
        newState = previousState != currentState
        this.currentState = currentState as WetterComState
        return currentState
    }

    override fun triggers(): Boolean {
        return newState
    }

    override fun output(reaction: Reaction): Output {
        val output = DefaultOutput("The weather (according to wetter.com) on %s".format(dateFormatter.format(currentState.dateTime.toInstant().toEpochMilli())))
        output.addText("text/html", htmlOutput())
        return output
    }

    private fun htmlOutput(): String {
        return createHTML().html {
            head {
                style("text/css") {
                    unsafe {
                        +".hour-state { display: table-row; }"
                        +" "
                        +".hour-state > div { display: table-cell; padding-right: 1em; }"
                    }
                }
            }
            body {
                val startTime = currentState.dateTime.toInstant()
                h1 { +"The Weather (according to wetter.com) on %s".format(dateFormatter.format(startTime.toEpochMilli())) }
                currentState.forEach {
                    div("hour-state") {
                        div("time") { +"%tH:%<tM".format(startTime.plus(it.hourIndex.toLong(), HOURS).toEpochMilli()) }
                        div("temperature") { +"%d °C".format(it.temperature.toInt()) }
                        div("rain-probability") { +"%d%%".format((it.rainProbability * 100).toInt()) }
                        div("rain-amount") { +"%d l/m²".format(it.rainAmount.toInt()) }
                        div("wind-direction") {
                            div {
                                attributes += "style" to "padding: none; transform: rotate(%ddeg)".format(180 - it.windDirection.degrees)
                                +"➠"
                            }
                        }
                        div("wind-speed") { +"%d km/h".format(it.windSpeed.toInt()) }
                        div("description") { +it.description }
                        div("image") { img(src = it.image) }
                    }
                }
            }
        }.toString()
    }

}
