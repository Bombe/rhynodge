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

import static com.google.common.base.Preconditions.*;

import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.HtmlState;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;

/**
 * {@link Filter} implementation that can extract {@link ComicState}s from
 * {@link HtmlState}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class ComicSiteFilter implements Filter {

	@Override
	public State filter(State state) {
		checkArgument(state instanceof HtmlState, "state must be an HTML state");

		/* initialize states: */
		HtmlState htmlState = (HtmlState) state;
		ComicState comicState = new ComicState();

		/* extract comics. */
		Optional<String> title = extractTitle(htmlState.document());
		List<String> imageUrls = extractImageUrls(htmlState.document());

		/* store comic, if found, into state. */
		if (title.isPresent() && !imageUrls.isEmpty()) {
			Comic comic = new Comic(title.get());
			for (String imageUrl : imageUrls) {
				comic.addImageUrl(imageUrl);
			}
			comicState.add(comic);
		}

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

}
