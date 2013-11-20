/*
 * rhynodge - AbstruseGooseComicFilterTest.java - Copyright © 2013 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.rhynodge.filters.comics;

import static com.google.common.base.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.base.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Unit test for {@link AbstruseGooseComicFilter}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AbstruseGooseComicFilterTest {

	private final AbstruseGooseComicFilter abstruseGooseComicFilter = new AbstruseGooseComicFilter();
	private final Document document;

	public AbstruseGooseComicFilterTest() throws IOException {
		document = loadDocument("/comics/abstrusegoose.html", "http://abstrusegoose.com/");
	}

	private Document loadDocument(String resourceName, String baseUri) throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(resourceName);
		Document document = Jsoup.parse(inputStream, "UTF-8", baseUri);
		return document;
	}

	@Test
	public void extractsComicTitleCorrectly() {
		Optional<String> title = abstruseGooseComicFilter.extractTitle(document);
		assertThat(title, is(of("Bizarro")));
	}

	@Test
	public void extractComicImagesCorrectly() {
		List<String> images = abstruseGooseComicFilter.extractImageUrls(document);
		assertThat(images, contains("http://abstrusegoose.com/strips/bizero.png"));
	}

	@Test
	public void extractImageCommentsCorrectly() {
		List<String> comments = abstruseGooseComicFilter.extractImageComments(document);
		assertThat(comments, contains("In the additive group of the integers, bizarro zero is... well... zero."));
	}

}
