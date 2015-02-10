package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import com.google.common.base.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link ChiefOBrienAtWorkComicFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ChiefOBrienAtWorkComicFilterTest {

	private final ChiefOBrienAtWorkComicFilter filter = new ChiefOBrienAtWorkComicFilter();
	private final Document document;

	public ChiefOBrienAtWorkComicFilterTest() throws IOException {
		document = ComicLoader.loadDocument("chief-obrien-at-work.html", "http://chiefobrienatwork.com/");
	}

	@Test
	public void filterCanParseComics() {
		MatcherAssert.assertThat(filter.extractImageUrls(document), Matchers.contains("http://41.media.tumblr.com/db92f4218b8a100f216863ce980e19a9/tumblr_njaewe7vNU1tjd8fao1_1280.jpg"));
	}

	@Test
	public void filterReturnsEmptyListForImageComments() {
		MatcherAssert.assertThat(filter.extractImageComments(document), Matchers.empty());
	}

	@Test
	public void filterCanParseComicTitles() {
		MatcherAssert.assertThat(filter.extractTitle(document), Matchers.is(Optional.of("EPISODE 60: Being Human\u00a0")));
	}

}
