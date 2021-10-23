package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.DrugsAndWiresComicFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.triggers.NewComicTrigger

class DrugsAndWiresWatcher: DefaultWatcher(query, filters, trigger)

private val query = HttpQuery("https://www.drugsandwires.fail/")
private val filters = listOf(HtmlFilter(), DrugsAndWiresComicFilter())
private val trigger = NewComicTrigger()
