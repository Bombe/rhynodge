package net.pterodactylus.rhynodge.filters.comics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;
import java.util.Objects;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link ScandinaviaAndTheWorldComicFilterTest}.
 *
 * @author <a href="mailto:david.roden@bietr.de">David Roden</a>
 */
public class ScandinaviaAndTheWorldComicFilterTest {

	private final Filter satwFilter = new ScandinaviaAndTheWorldComicFilter();
	private final HtmlState htmlState;

	public ScandinaviaAndTheWorldComicFilterTest() throws IOException {
		Document document = ResourceLoader.loadDocument(ScandinaviaAndTheWorldComicFilter.class, "scandinavia-and-the-world.html",
				"https://satwcomic.com/latest");
		htmlState = new HtmlState("https://satwcomic.com/latest", document);
	}

	@Test
	public void comicIsParsedCorrectly() {
		ComicState comicState = (ComicState) satwFilter.filter(htmlState);
		assertThat(comicState.comics(), contains(
				isComic("The whale in the room", contains(
						isStrip("http://satwcomic.com/art/the-whale-in-the-room.png", "")
				))
		));
	}

	private Matcher<? super Comic> isComic(String title, Matcher<Iterable<? extends Strip>> stripsMatcher) {
		return new TypeSafeDiagnosingMatcher<Comic>() {
			@Override
			protected boolean matchesSafely(Comic comic, Description mismatchDescription) {
				if (!Objects.equals(comic.title(), title)) {
					mismatchDescription.appendText("title is ").appendValue(comic.title());
					return false;
				}
				if (!stripsMatcher.matches(comic.strips())) {
					stripsMatcher.describeMismatch(comic.strips(), mismatchDescription);
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("is comic with title ").appendValue(title);
				description.appendText(" and strips ").appendValueList("(", ", ", ")", stripsMatcher);
			}
		};
	}

	private Matcher<? super Strip> isStrip(String url, String comment) {
		return new TypeSafeDiagnosingMatcher<Strip>() {
			@Override
			protected boolean matchesSafely(Strip strip, Description mismatchDescription) {
				if (!Objects.equals(strip.imageUrl(), url)) {
					mismatchDescription.appendText("image URL is ").appendValue(strip.imageUrl());
					return false;
				}
				if (!Objects.equals(strip.comment(), comment)) {
					mismatchDescription.appendText("comment is ").appendValue(strip.comment());
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("is strip from ").appendValue(url);
				description.appendText(" with comment ").appendValue(comment);
			}
		};
	}

}
