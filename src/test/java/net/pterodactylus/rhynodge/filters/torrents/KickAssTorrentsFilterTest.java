package net.pterodactylus.rhynodge.filters.torrents;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Unit test for {@link KickAssTorrentsFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class KickAssTorrentsFilterTest {

	private final KickAssTorrentsFilter filter = new KickAssTorrentsFilter();
	private final Document document;

	public KickAssTorrentsFilterTest() throws IOException {
		document = ResourceLoader.loadDocument(getClass(), "kickass-to-results.html", "https://kat.cr/");
	}

	@Test
	public void filterCanParseTorrentFiles() {
		Elements torrentRows = filter.getDataRows(document);

		MatcherAssert.assertThat(torrentRows, Matchers.contains(
				matchesTorrent("ubuntu 14.10 desktop amd64.iso", "1.08 GB", "magnet:?xt=urn:btih:2CCB0AFFD641FB6A4B5395C602BE71B43AB85B5A&dn=ubuntu+14+10+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/2CCB0AFFD641FB6A4B5395C602BE71B43AB85B5A.torrent?title=[kat.cr]ubuntu.14.10.desktop.amd64.iso", 1, 1, 0),
				matchesTorrent("ubuntu gnome 14.10 desktop amd64.iso", "998 MB", "magnet:?xt=urn:btih:DD2FD99B6DE623B14146EA32E659AAE44FE0E15D&dn=ubuntu+gnome+14+10+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/DD2FD99B6DE623B14146EA32E659AAE44FE0E15D.torrent?title=[kat.cr]ubuntu.gnome.14.10.desktop.amd64.iso", 1, 2, 0),
				matchesTorrent("Ubuntu Utopic 14.10 Server AMD64.iso", "582 MB", "magnet:?xt=urn:btih:EC5CE22050E0E3D7E7A279C241901B5BC1F36FE7&dn=ubuntu+utopic+14+10+server+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/EC5CE22050E0E3D7E7A279C241901B5BC1F36FE7.torrent?title=[kat.cr]ubuntu.utopic.14.10.server.amd64.iso", 1, 7, 0),
				matchesTorrent("Ubuntu Utopic 14.10 Server i386.iso", "576 MB", "magnet:?xt=urn:btih:108F0624B9FD0AB708FA284F6B1F3C3022BDEDD6&dn=ubuntu+utopic+14+10+server+i386+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/108F0624B9FD0AB708FA284F6B1F3C3022BDEDD6.torrent?title=[kat.cr]ubuntu.utopic.14.10.server.i386.iso", 1, 7, 0),
				matchesTorrent("ubuntu 14.10 desktop amd64.iso", "1.08 GB", "magnet:?xt=urn:btih:F7D6ECA0E176901119A7457C917A9A3547CB3CC3&dn=ubuntu+14+10+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/F7D6ECA0E176901119A7457C917A9A3547CB3CC3.torrent?title=[kat.cr]ubuntu.14.10.desktop.amd64.iso", 1, 0, 0),
				matchesTorrent("Ubuntu 14.10 Desktop 32bit ISO", "1.11 GB", "magnet:?xt=urn:btih:1619ECC9373C3639F4EE3E261638F29B33A6CBD6&dn=ubuntu+14+10+desktop+32bit+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/1619ECC9373C3639F4EE3E261638F29B33A6CBD6.torrent?title=[kat.cr]ubuntu.14.10.desktop.32bit.iso", 1, 87, 15),
				matchesTorrent("Ubuntu 14.10 Desktop 64bit ISO", "1.08 GB", "magnet:?xt=urn:btih:B415C913643E5FF49FE37D304BBB5E6E11AD5101&dn=ubuntu+14+10+desktop+64bit+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/B415C913643E5FF49FE37D304BBB5E6E11AD5101.torrent?title=[kat.cr]ubuntu.14.10.desktop.64bit.iso", 1, 150, 8),
				matchesTorrent("ubuntu gnome 14.10 beta2 desktop amd64.iso", "993 MB", "magnet:?xt=urn:btih:5D31B3DD8FA36E04B8FEA196F133056672567BCA&dn=ubuntu+gnome+14+10+beta2+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/5D31B3DD8FA36E04B8FEA196F133056672567BCA.torrent?title=[kat.cr]ubuntu.gnome.14.10.beta2.desktop.amd64.iso", 1, 0, 0),
				matchesTorrent("ubuntukylin 14.10 beta2 desktop amd64.iso", "1.27 GB", "magnet:?xt=urn:btih:9EDE5CF4F404030251F8319234CE81157D4FE001&dn=ubuntukylin+14+10+beta2+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/9EDE5CF4F404030251F8319234CE81157D4FE001.torrent?title=[kat.cr]ubuntukylin.14.10.beta2.desktop.amd64.iso", 1, 0, 0),
				matchesTorrent("ubuntukylin 14.10 beta2 desktop i386.iso", "1.28 GB", "magnet:?xt=urn:btih:E97417DB2F25D0FD87F7DF1E7B3B44CE1212F38E&dn=ubuntukylin+14+10+beta2+desktop+i386+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/E97417DB2F25D0FD87F7DF1E7B3B44CE1212F38E.torrent?title=[kat.cr]ubuntukylin.14.10.beta2.desktop.i386.iso", 1, 0, 0),
				matchesTorrent("ubuntustudio 14.10 beta2 dvd amd64.iso", "2.06 GB", "magnet:?xt=urn:btih:9230D72299A38213609104661181FE46918B6455&dn=ubuntustudio+14+10+beta2+dvd+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/9230D72299A38213609104661181FE46918B6455.torrent?title=[kat.cr]ubuntustudio.14.10.beta2.dvd.amd64.iso", 1, 0, 0),
				matchesTorrent("Ubuntustudio 14.10 Beta2 i386.iso", "2.19 GB", "magnet:?xt=urn:btih:6C50D760243D02D238726F6A62D6725F667E1AA9&dn=ubuntustudio+14+10+beta2+i386+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/6C50D760243D02D238726F6A62D6725F667E1AA9.torrent?title=[kat.cr]ubuntustudio.14.10.beta2.i386.iso", 1, 0, 0),
				matchesTorrent("Ubuntu 14.10 Desktop AMD64.iso", "1.1 GB", "magnet:?xt=urn:btih:18FC1F0208015A23BC08AD3AC5BDD2C46E20E8D0&dn=ubuntu+14+10+desktop+amd64+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/18FC1F0208015A23BC08AD3AC5BDD2C46E20E8D0.torrent?title=[kat.cr]ubuntu.14.10.desktop.amd64.iso", 1, 0, 0),
				matchesTorrent("Ubuntu Utopic 14.10 Desktop i386.iso", "1.12 GB", "magnet:?xt=urn:btih:54ADDC06D2DD7BF33DDFA9DB49FEA1A79C990D44&dn=ubuntu+utopic+14+10+desktop+i386+iso&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337", "http://torcache.net/torrent/54ADDC06D2DD7BF33DDFA9DB49FEA1A79C990D44.torrent?title=[kat.cr]ubuntu.utopic.14.10.desktop.i386.iso", 1, 0, 0)
		));
	}

	private Matcher<? super Element> matchesTorrent(String name, String size, String magnetUri, String downloadUri, int fileCount, int seedCount, int leechCount) {
		return new TypeSafeDiagnosingMatcher<Element>() {
			@Override
			protected boolean matchesSafely(Element dataRow, Description mismatchDescription) {
				String extractedName = filter.extractName(dataRow);
				if (!extractedName.equals(name)) {
					mismatchDescription.appendText("name is ").appendValue(extractedName);
					return false;
				}
				String extractedSize = filter.extractSize(dataRow);
				if (!extractedSize.equals(size)) {
					mismatchDescription.appendText("size is ").appendValue(extractedSize);
					return false;
				}
				String extractedMagnetUri = filter.extractMagnetUri(dataRow);
				if (!extractedMagnetUri.equals(magnetUri)) {
					mismatchDescription.appendText("magnet is ").appendValue(extractedMagnetUri);
					return false;
				}
				String extractedDownloadUri = filter.extractDownloadUri(dataRow);
				if (!extractedDownloadUri.equals(downloadUri)) {
					mismatchDescription.appendText("download is ").appendValue(extractedDownloadUri);
					return false;
				}
				int extractedFileCount = filter.extractFileCount(dataRow);
				if (extractedFileCount != fileCount) {
					mismatchDescription.appendText("file count is ").appendValue(extractedFileCount);
					return false;
				}
				int extractedSeedCount = filter.extractSeedCount(dataRow);
				if (extractedSeedCount != seedCount) {
					mismatchDescription.appendText("seed count is ").appendValue(extractedSeedCount);
					return false;
				}
				int extractedLeechCount = filter.extractLeechCount(dataRow);
				if (extractedLeechCount != leechCount) {
					mismatchDescription.appendText("leech count is ").appendValue(extractedLeechCount);
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description
						.appendText("matches torrent with name ").appendValue(name)
						.appendText(", size ").appendValue(size)
						.appendText(", magnet ").appendValue(magnetUri)
						.appendText(", download ").appendValue(downloadUri)
						.appendText(", file count ").appendValue(fileCount)
						.appendText(", seed count ").appendValue(seedCount)
						.appendText(", leech count ").appendValue(leechCount);
			}
		};
	}

}
