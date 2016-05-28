package net.pterodactylus.rhynodge.webpages.weather

/**
 * The direction the wind comes from.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
enum class WindDirection(val arrow: String) {

    NONE("↺"),
    NORTH("↓"),
    NORTHNORTHEAST("↓↙"),
    NORTHEAST("↙"),
    EASTNORTHEAST("↙←"),
    EAST("←"),
    EASTSOUTHEAST("←↖"),
    SOUTHEAST("↖"),
    SOUTHSOUTHEAST("↖↑"),
    SOUTH("↑"),
    SOUTHSOUTHWEST("↑↗"),
    SOUTHWEST("↗"),
    WESTSOUTHWEST("↗→"),
    WEST("→"),
    WESTNORTHWEST("→↘"),
    NORTHWEST("↘"),
    NORTHNORTHWEST("↘↓")

}

fun String.toWindDirection(): WindDirection {
    return when (this) {
        "N", "Nord" -> WindDirection.NORTH
        "Nordnordost" -> WindDirection.NORTHNORTHEAST
        "NO", "Nordost" -> WindDirection.NORTHEAST
        "Ostnordost" -> WindDirection.EASTNORTHEAST
        "O", "Ost" -> WindDirection.EAST
        "Ostsüdost" -> WindDirection.EASTSOUTHEAST
        "SO", "Südost" -> WindDirection.SOUTHEAST
        "Südsüdost" -> WindDirection.SOUTHSOUTHEAST
        "S", "Süd" -> WindDirection.SOUTH
        "Südsüdwest" -> WindDirection.SOUTHSOUTHWEST
        "SW", "Südwest" -> WindDirection.SOUTHWEST
        "Westsüdwest" -> WindDirection.WESTSOUTHWEST
        "W", "West" -> WindDirection.WEST
        "Westnordwest" -> WindDirection.WESTNORTHWEST
        "NW", "Nordwest" -> WindDirection.NORTHWEST
        "Nordnordwest" -> WindDirection.NORTHNORTHWEST
        else -> WindDirection.NONE
    }
}
