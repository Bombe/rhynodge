package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import net.pterodactylus.rhynodge.filters.ResourceLoader;

import com.google.common.base.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link BusinessCatComicFilter}.
 */
public class BusinessCatComicFilterTest {

	private final BusinessCatComicFilter filter = new BusinessCatComicFilter();
	private final Document document;

	public BusinessCatComicFilterTest() throws IOException {
		document = ResourceLoader.loadDocument("business-cat.html", "http://www.businesscat.happyjar.com//");
	}

	@Test
	public void imageTitleCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractTitle(document), Matchers.is(Optional.of("Running Late")));
	}

	@Test
	public void imageUrlsCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractImageUrls(document), Matchers.contains("http://www.businesscat.happyjar.com/wp-content/uploads/2015/01/2015-01-23-Running-Late.png"));
	}

	@Test
	public void imageCommentsCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractImageComments(document), Matchers.empty());
	}

}
