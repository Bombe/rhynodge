package net.pterodactylus.rhynodge.webpages.weather.wettercom

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.watchers.DefaultWatcher

/**
 * [DefaultWatcher] implementation that gets the weather for Hamburg.
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class WetterComHamburgWatcher : DefaultWatcher(
        HttpQuery("http://www.wetter.com/wetter_aktuell/wettervorhersage/heute/deutschland/hamburg/DE0004130.html"),
        listOf(HtmlFilter(), WetterComFilter()),
        WetterComTrigger()
)
