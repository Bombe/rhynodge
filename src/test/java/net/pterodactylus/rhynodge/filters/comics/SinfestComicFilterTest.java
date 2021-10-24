package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Document;
import org.junit.Test;


/**
 * Unit test for {@link SinfestComicFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SinfestComicFilterTest {

	private final Filter sinfestFilter = new SinfestComicFilter();
	private final HtmlState htmlState;

	public SinfestComicFilterTest() throws IOException {
		Document document = ResourceLoader.loadDocument(SinfestComicFilter.class, "sinfest.html", "https://sinfest.xyz/");
		htmlState = new HtmlState("https://sinfest.xyz/", document);
	}

	@Test
	public void canParseComicsFromHtml() {
		State state = sinfestFilter.filter(htmlState);
		MatcherAssert.assertThat(state, Matchers.instanceOf(ComicState.class));
	}

	@Test
	public void imageUrlsAreParsedCorrectly() {
		ComicState comicState = (ComicState) sinfestFilter.filter(htmlState);
		MatcherAssert.assertThat(comicState.comics(), Matchers.contains(matchesComic("October 24, 2021: Unperson 33", "https://sinfest.xyz/btphp/comics/2021-10-24.jpg", "")));
	}

	private Matcher<Comic> matchesComic(String title, String url, String comment) {
		return new TypeSafeDiagnosingMatcher<Comic>() {
			@Override
			protected boolean matchesSafely(Comic comic, Description mismatchDescription) {
				if (!comic.title().equals(title)) {
					mismatchDescription.appendText("comic is named ").appendValue(comic.title());
					return false;
				}
				if (comic.strips().size() != 1) {
					mismatchDescription.appendText("comic has ").appendValue(comic.strips().size()).appendText(" strips");
					return false;
				}
				Strip strip = comic.strips().get(0);
				if (!strip.imageUrl().equals(url)) {
					mismatchDescription.appendText("image url is ").appendValue(strip.imageUrl());
					return false;
				}
				if (!strip.comment().equals(comment)) {
					mismatchDescription.appendText("comment is ").appendValue(strip.comment());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("comic named ").appendValue(title);
				description.appendText(" at ").appendValue(url);
				description.appendText(" with comment ").appendValue(comment);
			}
		};
	}

}
