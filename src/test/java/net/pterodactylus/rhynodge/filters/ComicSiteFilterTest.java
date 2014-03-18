package net.pterodactylus.rhynodge.filters;

import static com.google.common.base.Optional.fromNullable;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link ComicSiteFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComicSiteFilterTest {

	@Test(expected = IllegalArgumentException.class)
	public void comicSiteFilterRequiresHtmlState() {
		State state = new FailedState();
		ComicSiteFilter comicSiteFilter = new TestComicSiteFilter(null, null, null);
		comicSiteFilter.filter(state);
	}

	@Test
	public void htmlWithoutTitleIsNotRecognizedAsComic() {
		State state = new HtmlState("http://foo/", null);
		ComicSiteFilter comicSiteFilter = new TestComicSiteFilter(null, null, null);
		State newState = comicSiteFilter.filter(state);
		assertThat(newState instanceof ComicState, is(true));
	}

	@Test
	public void htmlWithTitleButWithoutImagesIsNotRecognizedAsComic() {
		State state = new HtmlState("http://foo/", null);
		ComicSiteFilter comicSiteFilter = new TestComicSiteFilter("Title", Collections.<String>emptyList(), null);
		State newState = comicSiteFilter.filter(state);
		assertThat(newState instanceof ComicState, is(true));
	}

	@Test
	public void everyUrlGetAStrip() {
		State state = new HtmlState("http://foo/", null);
		ComicSiteFilter comicSiteFilter = new TestComicSiteFilter("Title", asList("url1.gif", "url2.gif", "url3.gif"), asList("Comment 1", "Comment 2"));
		State newState = comicSiteFilter.filter(state);
		assertThat(newState instanceof ComicState, is(true));
		ComicState comicState = (ComicState) newState;
		assertThat(comicState.comics(), hasSize(1));
		Comic comic = comicState.comics().get(0);
		assertThat(comic.strips(), contains(new Strip("http://foo/url1.gif", "Comment 1"), new Strip("http://foo/url2.gif", "Comment 2"), new Strip("http://foo/url3.gif", "")));
	}

	@Test(expected = IllegalStateException.class)
	public void illegalUrlThrowsException() {
		State state = new HtmlState("http://foo/^", null);
		ComicSiteFilter comicSiteFilter = new TestComicSiteFilter("Title", asList("url1.gif"), Collections.<String>emptyList());
		comicSiteFilter.filter(state);
	}

	private static class TestComicSiteFilter extends ComicSiteFilter {

		private final String title;
		private final List<String> urls;
		private final List<String> comments;

		public TestComicSiteFilter(String title, List<String> urls, List<String> comments) {
			this.title = title;
			this.urls = urls;
			this.comments = comments;
		}

		@Override
		protected Optional<String> extractTitle(Document document) {
			return fromNullable(title);
		}

		@Override
		protected List<String> extractImageUrls(Document document) {
			return urls;
		}

		@Override
		protected List<String> extractImageComments(Document document) {
			return comments;
		}

	}

}
