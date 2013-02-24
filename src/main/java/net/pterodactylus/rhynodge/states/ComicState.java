/*
 * rhynodge - ComicState.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.states;

import java.util.Iterator;
import java.util.List;

import net.pterodactylus.rhynodge.states.ComicState.Comic;

import com.google.common.collect.Lists;

/**
 * {@link net.pterodactylus.rhynodge.State} that can store an arbitrary amout of
 * {@link Comic}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComicState extends AbstractState implements Iterable<Comic> {

	/** The comics in this state. */
	private List<Comic> comics = Lists.newArrayList();

	/**
	 * Returns the list of comics contained in this state.
	 *
	 * @return The list of comics in this state
	 */
	public List<Comic> comics() {
		return comics;
	}

	/**
	 * Adds the given comic to this state.
	 *
	 * @param comic
	 * 		The comic to add
	 * @return This comic state
	 */
	public ComicState add(Comic comic) {
		comics.add(comic);
		return this;
	}

	//
	// ITERABLE METHODS
	//

	@Override
	public Iterator<Comic> iterator() {
		return comics.iterator();
	}

	//
	// OBJECT METHODS
	//

	@Override
	public String toString() {
		return String.format("ComicState[comics=%s]", comics());
	}

	/**
	 * Defines a comic.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Comic implements Iterable<String> {

		/** The title of the comic. */
		private final String title;

		/** The URLs of the comic’s images. */
		private final List<String> imageUrls = Lists.newArrayList();

		/**
		 * Creates a new comic with the given title.
		 *
		 * @param title
		 * 		The title of the comic
		 */
		public Comic(String title) {
			this.title = title;
		}

		/**
		 * Returns the title of this comic.
		 *
		 * @return The title of this comic
		 */
		public String title() {
			return title;
		}

		/**
		 * Returns the URLs of this comic’s images.
		 *
		 * @return The URLs of this comic’s images
		 */
		public List<String> imageUrls() {
			return imageUrls();
		}

		/**
		 * Adds an image URL to this comic.
		 *
		 * @param imageUrl
		 * 		The URL of the comic image to add
		 * @return This comic
		 */
		public Comic addImageUrl(String imageUrl) {
			imageUrls.add(imageUrl);
			return this;
		}

		//
		// ITERABLE METHODS
		//

		@Override
		public Iterator<String> iterator() {
			return imageUrls.iterator();
		}

		//
		// OBJECT METHODS
		//

		@Override
		public int hashCode() {
			return title.hashCode() ^ imageUrls().hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Comic)) {
				return false;
			}
			Comic comic = (Comic) object;
			return title().equals(comic.title()) && imageUrls().equals(comic.imageUrls());
		}

		@Override
		public String toString() {
			return String.format("Comic[title=%s,imageUrls=%s]", title(), imageUrls());
		}

	}

}
