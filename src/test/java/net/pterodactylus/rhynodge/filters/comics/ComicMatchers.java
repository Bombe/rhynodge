package net.pterodactylus.rhynodge.filters.comics;

import java.util.Objects;

import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Matchers for comics.
 *
 * @author <a href="mailto:david.roden@bietr.de">David Roden</a>
 */
public class ComicMatchers {

	public static Matcher<? super Comic> isComic(String title, Matcher<Iterable<? extends Strip>> stripsMatcher) {
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

	public static Matcher<? super Strip> isStrip(String url, String comment) {
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
