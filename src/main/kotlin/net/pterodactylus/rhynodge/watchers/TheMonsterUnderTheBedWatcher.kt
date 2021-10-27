package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.*
import net.pterodactylus.rhynodge.filters.comics.*
import net.pterodactylus.rhynodge.mergers.ComicMerger
import net.pterodactylus.rhynodge.queries.*

class TheMonsterUnderTheBedWatcher : DefaultWatcher(query, filters, merger)

private val query = HttpQuery("https://themonsterunderthebed.net/")
private val filters = listOf(HtmlFilter(), TheMonsterUnderTheBedFilter())
private val merger = ComicMerger()
