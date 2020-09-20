package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

/**
 * Unit test for {@link HeldentageFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HeldentageFilterTest {

	private final HeldentageFilter heldentageFilter = new HeldentageFilter();
	private final HtmlState htmlState;

	public HeldentageFilterTest() throws IOException {
		Document document = ResourceLoader.loadDocument(HeldentageFilter.class, "heldentage.html", "http://www.der-flix.de/");
		htmlState = new HtmlState("http://www.der-flix.de/", document);
	}

	@Test
	public void comicIsParsedCorrectly() {
		ComicState comicState = (ComicState) heldentageFilter.filter(htmlState);
		assertThat(comicState.comics(), contains(
				ComicMatchers.isComic("", contains(
						ComicMatchers.isStrip("http://www.der-flix.de/images/heldentage/Tag_916.jpg", "")
				))
		));
	}

}
