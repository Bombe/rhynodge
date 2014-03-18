package net.pterodactylus.rhynodge.filters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.EpisodeState;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.junit.Test;

/**
 * Unit test for {@link EpisodeFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EpisodeFilterTest {

	private final EpisodeFilter episodeFilter = new EpisodeFilter();

	@Test
	public void processingAFailedStateReturnsAFailedState() {
		State state = new FailedState();
		State newState = episodeFilter.filter(state);
		assertThat(newState.success(), is(false));
	}

	@Test(expected = IllegalStateException.class)
	public void episodeFilterRequiresATorrentState() {
		State state = new HtmlState("http://foo/", null);
		episodeFilter.filter(state);
	}

	@Test
	public void episodeFilterRecognizesEpisodesCorrectly() {
		State torrentState = createTorrentState();
		State newState = episodeFilter.filter(torrentState);
		assertThat(newState instanceof EpisodeState, is(true));
	}

	private TorrentState createTorrentState() {
		TorrentState torrentState = new TorrentState();
		torrentState.addTorrentFile(new TorrentFile("NonEpisodeFile.avi", "123", "magnet:?1", "url1", 1, 2, 3));
		torrentState.addTorrentFile(new TorrentFile("Some.S01E01.Episode.avi", "234", "magnet:?2", "url2", 2, 3, 4));
		torrentState.addTorrentFile(new TorrentFile("More.S01E02.Episodes.avi", "345", "magnet:?3", "url3", 3, 4, 5));
		torrentState.addTorrentFile(new TorrentFile("Some.1x01.Episode.avi", "456", "magnet:?4", "url4", 4, 5, 6));
		torrentState.addTorrentFile(new TorrentFile("Broken.S01.Episodes.avi", "567", "magnet:?5", "url5", 5, 6, 7));
		return torrentState;
	}

}
