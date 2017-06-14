package net.pterodactylus.rhynodge.filters.comics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.filters.ResourceLoader;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.HtmlState;

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
		assertThat(state, Matchers.instanceOf(ComicState.class));
	}

	@Test
	public void comicIsParsedCorrectly() {
		ComicState comicState = (ComicState) smbcFilter.filter(htmlState);
		assertThat(comicState.comics(), hasSize(1));
		Comic comic = comicState.comics().get(0);
		assertThat(comic.title(), is(""));
		assertThat(comic.strips(), hasSize(2));
		assertThat(comic.strips().get(0).imageUrl(), is("http://www.smbc-comics.com/comics/1496144390-soonish6%20(1).png"));
		assertThat(comic.strips().get(0).comment(), is("It's not an old man rant if you put it in the mouths of children!"));
		assertThat(comic.strips().get(1).imageUrl(), is("http://smbc-comics.com/comics/1496144435-soonish6after.png"));
		assertThat(comic.strips().get(1).comment(), is(""));
	}

}
