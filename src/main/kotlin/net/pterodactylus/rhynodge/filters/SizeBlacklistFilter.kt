package net.pterodactylus.rhynodge.filters

import net.pterodactylus.rhynodge.Filter
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.TorrentState

/**
 * Blacklist that filters torrents with a certain size.
 */
class SizeBlacklistFilter(private val blacklistedSizes: Iterable<String> = listOf("313.97 MiB", "331.97 MiB", "716.05 MiB", "314.21 MiB")): Filter {

	override fun filter(state: State): State {
		val torrentState = state as? TorrentState ?: return FailedState()
		return TorrentState(torrentState.torrentFiles().filterNot { it.size() in blacklistedSizes })
	}

}
