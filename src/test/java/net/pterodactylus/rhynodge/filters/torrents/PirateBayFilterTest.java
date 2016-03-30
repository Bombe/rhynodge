package net.pterodactylus.rhynodge.filters.torrents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.TorrentState;

import org.hamcrest.Matchers;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link PirateBayFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PirateBayFilterTest {

	private final PirateBayFilter filter = new PirateBayFilter();
	private final Document document;
	private final HtmlState htmlState;

	public PirateBayFilterTest() throws IOException {
		document = ResourceLoader.loadDocument(getClass(), "pirate-bay-results.html", "http://uj3wazyk5u4hnvtk.onion/");
		htmlState = new HtmlState("http://uj3wazyk5u4hnvtk.onion/", document);
	}

	@Test
	public void filterReturnsTorrentState() {
		assertThat(filter.filter(htmlState), instanceOf(TorrentState.class));
	}

	@Test
	public void testTorrentStateContainsCorrentTorrents() {
		TorrentState newState = (TorrentState) filter.filter(htmlState);

		assertThat(newState.torrentFiles(), Matchers.contains(
				TorrentMatcher.isTorrent("ubuntu-14.04-desktop-amd64.iso", "964 MiB",
						"magnet:?xt=urn:btih:18ac50d74c61883b3ab4c40f5dd3e35f157de1a2&dn=ubuntu-14.04-desktop-amd64.iso&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
						3, 0),
				TorrentMatcher.isTorrent("ubuntu 14 04 3 desktop i386 iso (Server)", "1015 MiB",
						"magnet:?xt=urn:btih:58ad8b464a0a4114e1890492543b6388d50af04a&dn=ubuntu+14+04+3+desktop+i386+iso+%28Server%29&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
						1, 0),
				TorrentMatcher.isTorrent("ubuntu-gnome-14.04-beta1-desktop-amd64.iso", "890 MiB",
						"magnet:?xt=urn:btih:baab5076469a52ea9f0d3eeae8263f154da2fbab&dn=ubuntu-gnome-14.04-beta1-desktop-amd64.iso&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
						0, 0),
				TorrentMatcher.isTorrent("Ubuntu 14.04.1 Server 64 Bit DE ISO", "572 MiB",
						"magnet:?xt=urn:btih:236fc296473ae52b2ff0c46a8de5ae04236cc774&dn=Ubuntu+14.04.1+Server+64+Bit+DE+ISO&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
						0, 0),
				TorrentMatcher.isTorrent("Ubuntu 14.04.1 Server 32 Bit DE ISO", "552 MiB",
						"magnet:?xt=urn:btih:2c0cef0ca97e31dceae9ddf48fe0d85f1fdb4550&dn=Ubuntu+14.04.1+Server+32+Bit+DE+ISO&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
						0, 0)
		));
	}

}
