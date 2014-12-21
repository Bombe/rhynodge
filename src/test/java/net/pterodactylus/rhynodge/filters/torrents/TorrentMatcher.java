package net.pterodactylus.rhynodge.filters.torrents;

import java.util.Objects;

import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * {@link Matcher}s for {@link TorrentFile}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentMatcher {

	public static Matcher<TorrentFile> isTorrent(String name, String size, int seedCount, int leechCount) {
		return new TypeSafeDiagnosingMatcher<TorrentFile>() {
			@Override
			protected boolean matchesSafely(TorrentFile torrentFile, Description mismatchDescription) {
				if (!torrentFile.name().equals(name)) {
					mismatchDescription.appendText("name is ").appendValue(torrentFile.name());
					return false;
				}
				if (!torrentFile.size().equals(size)) {
					mismatchDescription.appendText("size is ").appendValue(torrentFile.size());
					return false;
				}
				if (torrentFile.seedCount() != seedCount) {
					mismatchDescription.appendText("seed count is ").appendValue(torrentFile.seedCount());
					return false;
				}
				if (torrentFile.leechCount() != leechCount) {
					mismatchDescription.appendText("leech count is ").appendValue(torrentFile.leechCount());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("torrent named ").appendValue(name);
				description.appendText(", size ").appendValue(size);
				description.appendText(", seeds ").appendValue(seedCount);
				description.appendText(", leechs ").appendValue(leechCount);
			}
		};
	}

	public static Matcher<TorrentFile> isTorrent(String name, String size, String magnetLink, int seedCount, int leechCount) {
		return Matchers.allOf(isTorrent(name, size, seedCount, leechCount), torrentWithMagnetLink(magnetLink));
	}

	private static Matcher<? super TorrentFile> torrentWithMagnetLink(String magnetLink) {
		return new TypeSafeDiagnosingMatcher<TorrentFile>() {
			@Override
			protected boolean matchesSafely(TorrentFile item, Description mismatchDescription) {
				if (!Objects.equals(item.magnetUri(), magnetLink)) {
					mismatchDescription.appendText("magnet URI is ").appendValue(item.magnetUri());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("magnet URI is ").appendValue(magnetLink);
			}
		};
	}

}
