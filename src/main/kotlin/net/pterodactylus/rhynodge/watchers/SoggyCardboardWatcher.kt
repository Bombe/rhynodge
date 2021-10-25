package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.SoggyCardboardComicFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

class SoggyCardboardWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("http://www.soggycardboard.com/")
private val filters = listOf(HtmlFilter(), SoggyCardboardComicFilter())
private val merger = ComicMerger()
