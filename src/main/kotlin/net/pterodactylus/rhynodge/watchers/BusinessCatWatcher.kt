package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.BusinessCatComicFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

class BusinessCatWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://www.businesscatcomic.com/")
private val filters = listOf(HtmlFilter(), BusinessCatComicFilter())
private val merger = ComicMerger()
