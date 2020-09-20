package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.BusinessCatComicFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.triggers.NewComicTrigger

class BusinessCatWatcher : DefaultWatcher(query, filters, trigger)

private val query = HttpQuery("https://www.businesscatcomic.com/")
private val filters = listOf(HtmlFilter(), BusinessCatComicFilter())
private val trigger = NewComicTrigger()
