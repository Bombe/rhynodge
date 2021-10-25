package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.DrugsAndWiresComicFilter
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.HttpQuery

class DrugsAndWiresWatcher: DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://www.drugsandwires.fail/")
private val filters = listOf(HtmlFilter(), DrugsAndWiresComicFilter())
private val merger = ComicMerger()
