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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

/**
 * {@link net.pterodactylus.rhynodge.State} that can store an arbitrary amout of
 * {@link Comic}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComicState extends AbstractState implements Iterable<Comic> {

	/** The comics in this state. */
	@JsonProperty
	private final List<Comic> comics = Lists.newArrayList();

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
	public static class Comic implements Iterable<Strip> {

		/** The title of the comic. */
		@JsonProperty
		private final String title;

		/** The strips of the comic. */
		@JsonProperty
		private final List<Strip> strips = Lists.newArrayList();

		/**
		 * Creates a new comic with the given title.
		 *
		 * @param title
		 * 		The title of the comic
		 */
		public Comic(@JsonProperty("title") String title) {
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
		 * Returns the strips of this comic.
		 *
		 * @return The strips of this comic
		 */
		public List<Strip> strips() {
			return strips;
		}

		/**
		 * Adds a strip to this comic.
		 *
		 * @param strip
		 * 		The strip to add
		 * @return This comic
		 */
		public Comic add(Strip strip) {
			strips.add(strip);
			return this;
		}

		//
		// ITERABLE METHODS
		//

		@Override
		public Iterator<Strip> iterator() {
			return strips.iterator();
		}

		//
		// OBJECT METHODS
		//

		@Override
		public int hashCode() {
			return title.hashCode() ^ strips().hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Comic)) {
				return false;
			}
			Comic comic = (Comic) object;
			return title().equals(comic.title()) && strips().equals(comic.strips());
		}

		@Override
		public String toString() {
			return String.format("Comic[title=%s,strips=%s]", title(), strips());
		}

	}

	/**
	 * A strip is a single image that belongs to a comic.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Strip {

		/** The URL of the image. */
		@JsonProperty
		private final String imageUrl;

		/** The comment of the image. */
		@JsonProperty
		private final String comment;

		/**
		 * Creates a new strip.
		 *
		 * @param imageUrl
		 * 		The URL of the image
		 * @param comment
		 * 		The comment of the image
		 */
		public Strip(@JsonProperty("imageUrl") String imageUrl, @JsonProperty("comment") String comment) {
			this.imageUrl = imageUrl;
			this.comment = comment;
		}

		/**
		 * Returns the URL of the image.
		 *
		 * @return The URL of the image
		 */
		public String imageUrl() {
			return imageUrl;
		}

		/**
		 * Returns the comment of the image.
		 *
		 * @return The comment of the image
		 */
		public String comment() {
			return comment;
		}

		//
		// OBJECT METHODS
		//

		@Override
		public int hashCode() {
			return imageUrl().hashCode() ^ comment().hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Strip)) {
				return false;
			}
			Strip strip = (Strip) object;
			return imageUrl().equals(strip.imageUrl()) && comment().equals(strip.comment());
		}

		@Override
		public String toString() {
			return String.format("Strip[imageUrl=%s,comment=%s]", imageUrl(), comment());
		}

	}

}