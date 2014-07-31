package net.pterodactylus.rhynodge.filters.webpages.savoy;

import static java.time.LocalDateTime.of;
import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.jsoup.Jsoup.parse;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link MovieExtractor}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MovieExtractorTest {

	private final Document document;
	private final MovieExtractor movieExtractor = new MovieExtractor();
	private Collection<Movie> movies;

	public MovieExtractorTest() throws IOException {
		document = loadDocument("savoy.html", "http://foo");
		movies = movieExtractor.getMovies(document);
	}

	private Document loadDocument(String resourceName, String baseUri) throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(resourceName);
		return parse(inputStream, "UTF-8", baseUri);
	}

	@Test
	public void moviesAreLocated() throws IOException {
		assertThat(movies, notNullValue());
		assertThat(movies, containsInAnyOrder(
				movie("22 Jump Street (OV)", of(2014, 7, 30, 20, 15), of(2014, 7, 31, 20, 15), of(2014, 8, 1, 22, 30), of(2014, 8, 2, 20, 0), of(2014, 8, 2, 22, 45), of(2014, 8, 3, 17, 30), of(2014, 8, 4, 17, 30), of(2014, 8, 5, 20, 0)),
				movie("How to Train Your Dragon 2 (3D/OV)", of(2014, 7, 30, 17, 45), of(2014, 8, 1, 14, 15), of(2014, 8, 2, 17, 30), of(2014, 8, 3, 15, 0), of(2014, 8, 4, 20, 0)),
				movie("Jersey Boys (OV)", of(2014, 7, 31, 17, 15), of(2014, 8, 1, 16, 30), of(2014, 8, 3, 20, 0), of(2014, 8, 5, 17, 0)),
				movie("FILM CLUB presents: Ghostbusters (OV)", of(2014, 8, 1, 19, 30)),
				movie("Transformers: Age of Extinction (3D/OV)", of(2014, 8, 2, 14, 0)),
				movie("Dawn of the Planet of the Apes (3D/OV)"),
				movie("Traumkino: Yves Saint Laurent (Deutsche Fassung)"),
				movie("Hector and the Search for Happiness (OV)"),
				movie("The Expendables 3 (OV)"),
				movie("Doctor Who: Deep Breath"),
				movie("Traumkino: Boyhood (Deutsche Sprachfassung)")
		));
	}

	private Matcher<Movie> movie(String name, LocalDateTime... presentationTimes) {
		return new TypeSafeDiagnosingMatcher<Movie>() {
			@Override
			protected boolean matchesSafely(Movie movie, Description mismatchDescription) {
				if (!movie.getName().equals(name)) {
					mismatchDescription.appendText("movie is named ").appendValue(movie.getName());
					return false;
				}
				List<TicketLink> ticketLinks = new ArrayList<>(movie.getTicketLinks());
				if (ticketLinks.size() != presentationTimes.length) {
					mismatchDescription.appendText("has ").appendValue(ticketLinks.size()).appendText(" presentations");
					return false;
				}
				for (LocalDateTime presentationTime : presentationTimes) {
					Optional<TicketLink> foundTicketLink = empty();
					for (TicketLink ticketLink : ticketLinks) {
						if (ticketLink.getPresentationTime().equals(presentationTime)) {
							foundTicketLink = Optional.of(ticketLink);
							break;
						}
					}
					if (!foundTicketLink.isPresent()) {
						mismatchDescription.appendValue("has no presentation at ").appendValue(presentationTime);
						return false;
					}
					ticketLinks.remove(foundTicketLink.get());
				}
				if (!ticketLinks.isEmpty()) {
					mismatchDescription.appendText("has no presentations at ").appendValueList("", ", ", "", ticketLinks);
					return false;
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("movie with name ").appendValue(name);
				description.appendText(" and ").appendValue(presentationTimes.length).appendText(" presentations");
			}
		};
	}

}
