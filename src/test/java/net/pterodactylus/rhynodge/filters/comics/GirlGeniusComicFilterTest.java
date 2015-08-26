package net.pterodactylus.rhynodge.filters.comics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link GirlGeniusComicFilterTest}.
 *
 * @author <a href="mailto:david.roden@bietr.de">David Roden</a>
 */
public class GirlGeniusComicFilterTest {

	private final Filter filter = new GirlGeniusComicFilter();
	private final HtmlState htmlState;

	public GirlGeniusComicFilterTest() throws IOException {
		Document document = ResourceLoader.loadDocument(GirlGeniusComicFilter.class, "girl-genius.html", "http://www.girlgeniusonline.com/comic.php");
		htmlState = new HtmlState("http://www.girlgeniusonline.com/comic.php", document);
	}

	@Test
	public void comicCanBeParsed() {
		ComicState comicState = (ComicState) filter.filter(htmlState);
		assertThat(comicState.comics(), contains(
				ComicMatchers.isComic("", contains(
						ComicMatchers.isStrip("http://www.girlgeniusonline.com/ggmain/strips/ggmain20150824.jpg", "")
				))
		));
	}

}
