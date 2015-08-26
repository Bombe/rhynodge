package net.pterodactylus.rhynodge.filters.comics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link CyanideAndHappinessComicFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class CyanideAndHappinessComicFilterTest {

	private final CyanideAndHappinessComicFilter comicSiteFilter = new CyanideAndHappinessComicFilter();
	private final Document document;
	private final HtmlState htmlState;

	public CyanideAndHappinessComicFilterTest() throws IOException {
		document = ResourceLoader.loadDocument(getClass(), "cyanide-and-happiness.html", "http://www.explosm.net/comics/new/");
		htmlState = new HtmlState("http://files.explosm.net/comics/Kris/skeletor.png", document);
	}

	@Test
	public void comicCanBeParsed() {
		ComicState comicState = (ComicState) comicSiteFilter.filter(htmlState);
		assertThat(comicState.comics(), contains(
				ComicMatchers.isComic("", contains(
						ComicMatchers.isStrip("http://files.explosm.net/comics/Kris/skeletor.png", "")
				))
		));
	}

}
