package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.ChiefOBrienAtWorkComicFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

@Suppress("unused")
class ChiefOBrienAtWorkWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://chiefobrienatwork.com/")
private val filters = listOf(HtmlFilter(), ChiefOBrienAtWorkComicFilter())
private val merger = ComicMerger()
