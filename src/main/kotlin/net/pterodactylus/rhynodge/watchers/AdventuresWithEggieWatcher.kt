package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.AdventuresWithEggieComicSiteFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

@Suppress("unused")
class AdventuresWithEggieWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://adventureswitheggie.com/")
private val filters = listOf(HtmlFilter(), AdventuresWithEggieComicSiteFilter())
private val merger = ComicMerger()
