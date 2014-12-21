package net.pterodactylus.rhynodge.filters.torrents;

import java.io.IOException;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.TorrentState;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link TorrentzEuFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentzEuFilterTest {

	private final TorrentzEuFilter torrentzEuFilter = new TorrentzEuFilter();

	@Test
	public void filterCanExtractAllTorrents() throws IOException {
		Document document = Jsoup.parse(getClass().getResourceAsStream("torrentz-eu-results.html"), "UTF-8", "http://torrentz.eu/searchA?f=Ubuntu+ISO");
		HtmlState htmlState = new HtmlState("http://torrentz.eu/searchA?f=Ubuntu+ISO", document);
		State newState = torrentzEuFilter.filter(htmlState);
		MatcherAssert.assertThat(newState, Matchers.notNullValue());
		MatcherAssert.assertThat(newState, Matchers.instanceOf(TorrentState.class));
		TorrentState torrentState = (TorrentState) newState;
		MatcherAssert.assertThat(torrentState.torrentFiles(), Matchers.hasSize(2));
		MatcherAssert.assertThat(torrentState.torrentFiles(), Matchers.contains(
				TorrentMatcher.isTorrent("Ubuntu 14 04 64Bit ISO File", "981 MB", 0, 1),
				TorrentMatcher.isTorrent("Ubuntu 14 04 LTS Desktop 64bit ISO", "964 MB", 75, 5)
		));
	}

}
