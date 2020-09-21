package net.pterodactylus.rhynodge.webpages.weather

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.img
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.unsafe
import net.pterodactylus.rhynodge.states.AbstractState
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Contains a weather state.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WeatherState(val service: String, val dateTime: ZonedDateTime) : AbstractState(true), Iterable<HourState> {

	constructor(@JsonProperty("service") service: String, @JsonProperty("dateTime") time: Long) :
			this(service, Instant.ofEpochMilli(time).atZone(ZoneId.of("Europe/Berlin")))

	@JsonProperty("hours")
	val hours: List<HourState> = mutableListOf()

	@get:JsonGetter("dateTime")
	val timeMillis = dateTime.toInstant().toEpochMilli()

	operator fun plusAssign(hourState: HourState) {
		(hours as MutableList<HourState>).add(hourState)
	}

	override fun iterator(): Iterator<HourState> {
		return hours.iterator()
	}

	override fun equals(other: Any?): Boolean {
		other as? WeatherState ?: return false
		return (dateTime == other.dateTime) and (hours == other.hours)
	}

	override fun plainText(): String=""

	override fun htmlText(): String =
			createHTML().html {
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
					val startTime = dateTime.toInstant()
					h1 { +"The Weather (according to wetter.de) on %s".format(dateFormatter.format(startTime.toEpochMilli())) }
					val showFeltTemperature = any { it.feltTemperature != null }
					val showGustSpeed = any { it.gustSpeed != null }
					val showHumidity = any { it.humidity != null }
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
						forEach {
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

private val dateFormatter: DateFormat =
        DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)

private fun Double.minDigits(): String =
		toString().replace(Regex("\\.0*$"), "")
