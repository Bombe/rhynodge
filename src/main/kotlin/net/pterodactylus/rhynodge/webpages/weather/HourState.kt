package net.pterodactylus.rhynodge.webpages.weather

import com.fasterxml.jackson.annotation.JsonProperty
import net.pterodactylus.rhynodge.webpages.weather.WindDirection

/**
 * Container for weather conditions of a single hour.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
data class HourState(
        @JsonProperty("hourIndex") val hourIndex: Int,
        @JsonProperty("temperature") val temperature: Int,
        @JsonProperty("feltTemperature") val feltTemperature: Int? = null,
        @JsonProperty("rainProbability") val rainProbability: Double,
        @JsonProperty("rainAmount") val rainAmount: Double,
        @JsonProperty("windDirection") val windDirection: WindDirection,
        @JsonProperty("windSpeed") val windSpeed: Int,
        @JsonProperty("gustSpeed") val gustSpeed: Int? = null,
        @JsonProperty("humidity") val humidity: Double? = null,
        @JsonProperty("description") val description: String,
        @JsonProperty("image") val image: String)
