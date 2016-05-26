package net.pterodactylus.rhynodge.webpages.weather.wettercom

/**
 * Container for weather conditions of a single hour.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
data class HourState(val hourIndex: Int, val temperature: Double, val rainProbability: Double, val rainAmount: Double, val windDirection: WindDirection, val windSpeed: Double, val description: String, val image: String) {

    class Builder(private val hourIndex: Int) {

        fun temperature(temperature: Double) = _1(temperature)

        inner class _1(private val temperature: Double) {

            fun rainProbability(rainProbability: Double) = _2(rainProbability)

            inner class _2(private val rainProbability: Double) {

                fun rainAmount(rainAmount: Double) = _3(rainAmount)

                inner class _3(private val rainAmount: Double) {

                    fun windFrom(windDirection: WindDirection) = _4(windDirection);

                    inner class _4(private val windDirection: WindDirection) {

                        fun at(windSpeed: Double) = _5(windSpeed)

                        inner class _5(private val windSpeed: Double) {

                            fun describedAs(description: String) = _6(description)

                            inner class _6(private val description: String) {

                                fun withImage(imageUrl: String) = _7(imageUrl)

                                inner class _7(private val imageUrl: String) {

                                    fun build(): HourState {
                                        return HourState(hourIndex, temperature, rainProbability, rainAmount, windDirection, windSpeed, description, imageUrl)
                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    companion object {

        fun atHour(hourIndex: Int) = Builder(hourIndex)

    }

}
