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
		document = ResourceLoader.loadDocument(getClass(), "cyanide-and-happiness.html", "https://explosm.net/");
		htmlState = new HtmlState("https://explosm.net/", document);
	}

	@Test
	public void comicCanBeParsed() {
		ComicState comicState = (ComicState) comicSiteFilter.filter(htmlState);
		assertThat(comicState.comics(), contains(
				ComicMatchers.isComic("", contains(
						ComicMatchers.isStrip("https://static.explosm.net/2022/06/11022918/bringaparent.png", "")
				))
		));
	}

}
