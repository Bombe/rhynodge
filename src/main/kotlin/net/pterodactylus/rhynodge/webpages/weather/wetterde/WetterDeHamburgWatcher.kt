package net.pterodactylus.rhynodge.webpages.weather.wetterde

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.watchers.DefaultWatcher

/**
 * [DefaultWatcher] implementation that gets the weather for Hamburg.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterDeHamburgWatcher : DefaultWatcher(
        HttpQuery("http://www.wetter.de/deutschland/wetter-hamburg-18219464/wetterbericht-aktuell.html"),
        listOf(HtmlFilter(), WetterDeFilter()),
        WetterDeTrigger()
)
