package net.pterodactylus.rhynodge.filters

import net.pterodactylus.rhynodge.states.FailedState
import net.pterodactylus.rhynodge.states.TorrentState
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Test

/**
 * Unit test for [SizeBlacklistFilter].
 */
class SizeBlacklistFilterTest {

	private val filter = SizeBlacklistFilter(listOf("123 MiB", "234 GiB"))
	private val notMatchingTorrentFile1 = TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-3LT0N", "123 GiB", "magnet:?xt=a", "http://", 1, 2, 3)
	private val notMatchingTorrentFile2 = TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-HELLRAZ0R", "123 MB", "magnet:?xt=b", "http://", 1, 2, 3)
	private val matchingTorrentFile1 = TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-Haggebulle", "123 MiB", "magnet:?xt=c", "http://", 1, 2, 3)
	private val matchingTorrentFile2 = TorrentFile("The Vampire Diaries S05E05 2013 HDRip 720p-FooKaS", "234 GiB", "magnet:?xt=d", "http://", 1, 2, 3)

	@Test
	fun `filter removes the correct torrents`() {
		val filteredState = filter.filter(TorrentState(listOf(
				notMatchingTorrentFile1,
				notMatchingTorrentFile2,
				matchingTorrentFile1,
				matchingTorrentFile2
		))) as TorrentState
		assertThat(filteredState.torrentFiles(), Matchers.containsInAnyOrder(
				notMatchingTorrentFile1,
				notMatchingTorrentFile2
		))
	}

	@Test
	fun `filter returns failed state for a failed state`() {
	    assertThat(filter.filter(FailedState()).success(), equalTo(false))
	}

}
