package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.SoggyCardboardComicFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.triggers.NewComicTrigger

class SoggyCardboardWatcher : DefaultWatcher(query, filters, trigger)

private val query = HttpQuery("http://www.soggycardboard.com/")
private val filters = listOf(HtmlFilter(), SoggyCardboardComicFilter())
private val trigger = NewComicTrigger()
