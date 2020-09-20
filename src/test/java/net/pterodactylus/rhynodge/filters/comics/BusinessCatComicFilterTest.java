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
		document = ResourceLoader.loadDocument(getClass(), "business-cat.html", "https://www.businesscatcomic.com/");
	}

	@Test
	public void imageTitleCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractTitle(document), Matchers.is(Optional.of("Full Circle")));
	}

	@Test
	public void imageUrlsCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractImageUrls(document), Matchers.contains("https://www.businesscatcomic.com/wp-content/uploads/2019/11/2018-09-07-Full-Circle.png"));
	}

	@Test
	public void imageCommentsCanBeExtracted() {
		MatcherAssert.assertThat(filter.extractImageComments(document), Matchers.empty());
	}

}
