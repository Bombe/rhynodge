package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;

import com.google.common.base.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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

	public CyanideAndHappinessComicFilterTest() throws IOException {
		document = ResourceLoader.loadDocument("cyanide-and-happiness.html", "http://www.explosm.net/comics/new/");
	}

	@Test
	public void filterCanParseComicTitle() {
		MatcherAssert.assertThat(comicSiteFilter.extractTitle(document), Matchers.is(Optional.absent()));
	}

	@Test
	public void filterCanExtractImageUrls() {
		MatcherAssert.assertThat(comicSiteFilter.extractImageUrls(document), Matchers.contains("http://files.explosm.net/comics/Dave/moneyhappiness.png"));
	}

	@Test
	public void filterExtractNoImageComments() {
		MatcherAssert.assertThat(comicSiteFilter.extractImageComments(document), Matchers.empty());
	}

}
