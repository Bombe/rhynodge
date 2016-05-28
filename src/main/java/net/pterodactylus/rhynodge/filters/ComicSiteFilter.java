/*
 * rhynodge - ComicFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.filters;

import static com.google.common.base.Preconditions.checkArgument;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;

/**
 * {@link Filter} implementation that can extract {@link ComicState}s from
 * {@link HtmlState}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class ComicSiteFilter implements Filter {

	@NotNull
	@Override
	public State filter(@NotNull State state) {
		checkArgument(state instanceof HtmlState, "state must be an HTML state");

		/* initialize states: */
		HtmlState htmlState = (HtmlState) state;

		/* extract comics. */
		Optional<String> title = extractTitle(htmlState.document());
		List<String> imageUrls = extractImageUrls(htmlState.document());
		List<String> imageComments = extractImageComments(htmlState.document());

		/* store comic, if found, into state. */
		if (!title.isPresent() || imageUrls.isEmpty()) {
			return new FailedState();
		}

		ComicState comicState = new ComicState();
		Comic comic = new Comic(title.get());
		int imageCounter = 0;
		for (String imageUrl : imageUrls) {
			String imageComment = (imageCounter < imageComments.size()) ? imageComments.get(imageCounter) : "";
			try {
				URI stripUri = new URI(htmlState.uri()).resolve(imageUrl.replaceAll(" ", "%20"));
				Strip strip = new Strip(stripUri.toString(), imageComment);
				imageCounter++;
				comic.add(strip);
			} catch (URISyntaxException use1) {
				throw new IllegalStateException(String.format("Could not resolve image URL “%s” against base URL “%s”.", imageUrl, htmlState.uri()), use1);
			}
		}
		comicState.add(comic);

		return comicState;
	}

	//
	// PROTECTED METHODS
	//

	/**
	 * Extracts the title of the comic from the given document.
	 *
	 * @param document
	 * 		The document to extract the title from
	 * @return The extracted title, or {@link Optional#absent()}} if no title could
	 *         be found
	 */
	protected abstract Optional<String> extractTitle(Document document);

	/**
	 * Extracts the image URLs from the given document.
	 *
	 * @param document
	 * 		The document to extract the image URLs from
	 * @return The extracted image URLs, or an empty list if no URLs could be
	 *         found
	 */
	protected abstract List<String> extractImageUrls(Document document);

	/**
	 * Extracts the image comments from the given document. The elements of this
	 * last and of the list returned by {@link #extractImageUrls(org.jsoup.nodes.Document)}
	 * are paired up and added as {@link Strip}s. If the list returned by this
	 * method has less elements, an empty string is used for the remaining images.
	 *
	 * @param document
	 * 		The document to extract the image comments from
	 * @return The extracted image comments
	 */
	protected abstract List<String> extractImageComments(Document document);

}
