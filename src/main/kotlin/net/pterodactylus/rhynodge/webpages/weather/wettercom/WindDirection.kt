package net.pterodactylus.rhynodge.webpages.weather.wettercom

/**
 * The direction the wind comes from.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
enum class WindDirection(val arrow: String) {

    NONE("↺"),
    NORTH("↓"),
    NORTHEAST("↙"),
    EAST("←"),
    SOUTHEAST("↖"),
    SOUTH("↑"),
    SOUTHWEST("↗"),
    WEST("→"),
    NORTHWEST("↘")

}

fun String.toWindDirection(): WindDirection {
    return when (this) {
        "N" -> WindDirection.NORTH
        "NO" -> WindDirection.NORTHEAST
        "O" -> WindDirection.EAST
        "SO" -> WindDirection.SOUTHEAST
        "S" -> WindDirection.SOUTH
        "SW" -> WindDirection.SOUTHWEST
        "W" -> WindDirection.WEST
        "NW" -> WindDirection.NORTHWEST
        else -> WindDirection.NONE
    }
}
