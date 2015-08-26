package net.pterodactylus.rhynodge.filters.comics;

import static com.google.common.base.Optional.absent;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jsoup.Jsoup.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link HeldentageFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HeldentageFilterTest {

	private final HeldentageFilter heldentageFilter = new HeldentageFilter();
	private final Document document;

	public HeldentageFilterTest() throws IOException {
		document = loadDocument("heldentage.html", "http://www.der-flix.de/");
	}

	private Document loadDocument(String resourceName, String baseUri) throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(resourceName);
		Document document = parse(inputStream, "UTF-8", baseUri);
		return document;
	}

	@Test
	public void comicDoesNotHaveATitle() {
		assertThat(heldentageFilter.extractTitle(document), is(absent()));
	}

	@Test
	public void comicUrlCanBeFound() {
		assertThat(heldentageFilter.extractImageUrls(document), is(asList("/images/heldentage/Tag_916.jpg")));
	}

	@Test
	public void comicDoesNotHaveImageComments() {
		assertThat(heldentageFilter.extractImageComments(document), is(Collections.<String>emptyList()));
	}

}
