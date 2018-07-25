package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.*
import net.pterodactylus.rhynodge.filters.comics.*
import net.pterodactylus.rhynodge.queries.*
import net.pterodactylus.rhynodge.triggers.*

class TheMonsterUnderTheBedWatcher : DefaultWatcher(query, filters, trigger)

private val query = HttpQuery("http://themonsterunderthebed.net/")
private val filters = listOf(HtmlFilter(), TheMonsterUnderTheBedFilter())
private val trigger = NewComicTrigger()
