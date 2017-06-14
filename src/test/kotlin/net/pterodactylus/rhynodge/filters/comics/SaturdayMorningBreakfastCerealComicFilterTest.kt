package net.pterodactylus.rhynodge.filters.comics

import net.pterodactylus.rhynodge.filters.ResourceLoader
import net.pterodactylus.rhynodge.states.ComicState
import net.pterodactylus.rhynodge.states.HtmlState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test

/**
 * Unit test for [SaturdayMorningBreakfastCerealComicFilter].
 */
class SaturdayMorningBreakfastCerealComicFilterTest {

    private val smbcFilter = SaturdayMorningBreakfastCerealComicFilter()
    private val htmlState = ResourceLoader.loadDocument(SinfestComicFilter::class.java, "saturday-morning-breakfast-cereal.html", "http://www.smbc-comics.com/").let {
        HtmlState("http://www.smbc-comics.com/", it)
    }

    @Test
    fun htmlCanBeParsed() {
        val state = smbcFilter.filter(htmlState)
        assertThat(state, instanceOf(ComicState::class.java))
    }

    @Test
    fun comicIsParsedCorrectly() {
        val comicState = smbcFilter.filter(htmlState) as ComicState
        assertThat(comicState.comics(), hasSize(1))
        val comic = comicState.comics().first()
        assertThat(comic.title(), equalTo(""))
        assertThat(comic.strips(), hasSize(2))
        comic.strips().first().also { firstImage ->
            assertThat(firstImage.imageUrl(), equalTo("http://www.smbc-comics.com/comics/1496144390-soonish6%20(1).png"))
            assertThat(firstImage.comment(), equalTo("It's not an old man rant if you put it in the mouths of children!"))
        }
        comic.strips()[1].also { secondImage ->
            assertThat(secondImage.imageUrl(), equalTo("http://smbc-comics.com/comics/1496144435-soonish6after.png"))
            assertThat(secondImage.comment(), equalTo(""))
        }
    }

}
