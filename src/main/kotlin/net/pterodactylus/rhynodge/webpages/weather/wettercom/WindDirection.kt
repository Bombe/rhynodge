package net.pterodactylus.rhynodge.webpages.weather.wettercom

/**
 * The direction the wind comes from.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
enum class WindDirection(val degrees: Int) {

    NONE(0),
    NORTH(90),
    NORTHEAST(45),
    EAST(0),
    SOUTHEAST(315),
    SOUTH(270),
    SOUTHWEST(225),
    WEST(180),
    NORTHWEST(135)

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
