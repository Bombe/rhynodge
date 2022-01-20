package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.OglafComicSiteFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

@Suppress("unused")
class OglafWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://www.oglaf.com/")
private val filters = listOf(HtmlFilter(), OglafComicSiteFilter())
private val merger = ComicMerger()
