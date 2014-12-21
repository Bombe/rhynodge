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
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentHoundFilterTest {

	private final TorrentHoundFilter filter = new TorrentHoundFilter();

	@Test
	public void canParseResults() throws IOException {
		Document document = Jsoup.parse(getClass().getResourceAsStream("torrent-hound-results.html"), "UTF-8", "http://www.torrenthound.com/search/1/Ubuntu+14+04+amd64+ISO/added:desc");
		HtmlState htmlState = new HtmlState("http://www.torrenthound.com/search/1/Ubuntu+14+04+amd64+ISO/added:desc", document);
		State newState = filter.filter(htmlState);
		MatcherAssert.assertThat(newState, Matchers.notNullValue());
		MatcherAssert.assertThat(newState, Matchers.instanceOf(TorrentState.class));
		TorrentState torrentState = (TorrentState) newState;
		MatcherAssert.assertThat(torrentState.torrentFiles(), Matchers.hasSize(5));
		MatcherAssert.assertThat(torrentState.torrentFiles(), Matchers.contains(
				TorrentMatcher.isTorrent("Ubuntu 14 04 1 server amd64 ISO", "572.0 Mb", "magnet:?xl=599785472&xt=urn:btih:2d066c94480adcf52bfd1185a75eb4ddc1777673&dn=ubuntu+14+04+1+server+amd64+iso&tr=udp%3A%2F%2Ftracker.istole.it%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80", 5, 3),
				TorrentMatcher.isTorrent("Ubuntu 14 04 1 desktop amd64 ISO", "981.0 Mb", 34, 23),
				TorrentMatcher.isTorrent("Ubuntu 14 04 1 desktop amd64+mac ISO", "979.0 Mb", 1, 0),
				TorrentMatcher.isTorrent("Ubuntu gnome 14 04 1 desktop amd64 ISO", "935.0 Mb", 1, 1),
				TorrentMatcher.isTorrent("Ubuntu gnome 14.04 beta1 desktop amd64.ISO", "890.0 Mb", 2, 0)
		));
	}

}
