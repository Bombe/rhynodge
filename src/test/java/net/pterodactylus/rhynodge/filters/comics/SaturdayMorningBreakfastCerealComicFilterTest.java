package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.HtmlState;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link SaturdayMorningBreakfastCerealComicFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SaturdayMorningBreakfastCerealComicFilterTest {

	private final Filter smbcFilter = new SaturdayMorningBreakfastCerealComicFilter();
	private final HtmlState htmlState;

	public SaturdayMorningBreakfastCerealComicFilterTest() throws IOException {
		Document document = ResourceLoader.loadDocument(SinfestComicFilter.class, "saturday-morning-breakfast-cereal.html", "http://www.smbc-comics.com/");
		htmlState = new HtmlState("http://www.smbc-comics.com/", document);
	}

	@Test
	public void htmlCanBeParsed() {
		State state = smbcFilter.filter(htmlState);
		MatcherAssert.assertThat(state, Matchers.instanceOf(ComicState.class));
	}

	@Test
	public void comicIsParsedCorrectly() {
		ComicState comicState = (ComicState) smbcFilter.filter(htmlState);
		MatcherAssert.assertThat(comicState.comics(), Matchers.hasSize(1));
		Comic comic = comicState.comics().get(0);
		MatcherAssert.assertThat(comic.title(), Matchers.is(""));
		MatcherAssert.assertThat(comic.strips(), Matchers.hasSize(2));
		MatcherAssert.assertThat(comic.strips().get(0).imageUrl(), Matchers.is("http://www.smbc-comics.com/comics/1496144390-soonish6%20(1).png"));
		MatcherAssert.assertThat(comic.strips().get(0).comment(), Matchers.is("It's not an old man rant if you put it in the mouths of children!"));
		MatcherAssert.assertThat(comic.strips().get(1).imageUrl(), Matchers.is("http://smbc-comics.com/comics/1496144435-soonish6after.png"));
		MatcherAssert.assertThat(comic.strips().get(1).comment(), Matchers.is(""));
	}

}
