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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.states.ComicState.Comic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

/**
 * {@link net.pterodactylus.rhynodge.State} that can store an arbitrary amout of
 * {@link Comic}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComicState extends AbstractState implements Iterable<Comic> {

	@JsonProperty
	private final List<Comic> comics = Lists.newArrayList();
	private final Set<Comic> newComics = new HashSet<>();

	@SuppressWarnings("unused")
	// used for deserialization
	private ComicState() {
	}

	public ComicState(Collection<Comic> allComics) {
		this.comics.addAll(allComics);
	}

	public ComicState(Collection<Comic> allComics, Collection<Comic> newComics) {
		this(allComics);
		this.newComics.addAll(newComics);
	}

	@Override
	public boolean isEmpty() {
		return comics.isEmpty();
	}

	@Override
	public boolean triggered() {
		return !newComics.isEmpty();
	}

	public List<Comic> comics() {
		return comics;
	}

	@Override
	public Iterator<Comic> iterator() {
		return comics.iterator();
	}

	@Override
	public String toString() {
		return format("ComicState[comics=%s]", comics());
	}

	@Nonnull
	@Override
	protected String summary(Reaction reaction) {
		return format("New Comic found for “%s!”", reaction.name());
	}

	@Nonnull
	@Override
	protected String plainText() {
		StringBuilder text = new StringBuilder();

		for (Comic newComic : newComics) {
			text.append("Comic Found: ").append(newComic.title()).append("\n\n");
			for (Strip strip : newComic) {
				text.append("Image: ").append(strip.imageUrl()).append("\n");
				if (!StringUtils.isBlank(strip.comment())) {
					text.append("Comment: ").append(strip.comment()).append("\n");
				}
			}
			text.append("\n\n");
		}

		return text.toString();
	}

	@Nullable
	@Override
	protected String htmlText() {
		StringBuilder html = new StringBuilder();
		html.append("<body>");

		for (Comic newComic : newComics) {
			generateComicHtml(html, newComic);
		}

		List<Comic> latestComics = new ArrayList<>(comics());
		Collections.reverse(latestComics);
		int comicCount = 0;
		for (Comic comic : latestComics) {
			if (newComics.contains(comic)) {
				continue;
			}
			generateComicHtml(html, comic);
			if (++comicCount == 7) {
				break;
			}
		}

		return html.append("</body>").toString();
	}

	private void generateComicHtml(StringBuilder html, Comic comic) {
		html.append("<h1>").append(StringEscapeUtils.escapeHtml4(comic.title())).append("</h1>\n");
		for (Strip strip : comic) {
			html.append("<div><img src=\"").append(StringEscapeUtils.escapeHtml4(strip.imageUrl()));
			html.append("\" alt=\"").append(StringEscapeUtils.escapeHtml4(strip.comment()));
			html.append("\" title=\"").append(StringEscapeUtils.escapeHtml4(strip.comment()));
			html.append("\"></div>\n");
			html.append("<div>").append(StringEscapeUtils.escapeHtml4(strip.comment())).append("</div>\n");
		}
	}

	/**
	 * Defines a comic.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Comic implements Iterable<Strip> {

		@JsonProperty
		private final String title;

		@JsonProperty
		private final List<Strip> strips = Lists.newArrayList();

		public Comic(@JsonProperty("title") String title) {
			this.title = title;
		}

		public String title() {
			return title;
		}

		public List<Strip> strips() {
			return strips;
		}

		public Comic add(Strip strip) {
			strips.add(strip);
			return this;
		}

		@Override
		public Iterator<Strip> iterator() {
			return strips.iterator();
		}

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
			return format("Comic[title=%s,strips=%s]", title(), strips());
		}

	}

	/**
	 * A strip is a single image that belongs to a comic.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Strip {

		@JsonProperty
		private final String imageUrl;

		@JsonProperty
		private final String comment;

		public Strip(@JsonProperty("imageUrl") String imageUrl, @JsonProperty("comment") String comment) {
			this.imageUrl = imageUrl;
			this.comment = comment;
		}

		public String imageUrl() {
			return imageUrl;
		}

		public String comment() {
			return comment;
		}

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
			return format("Strip[imageUrl=%s,comment=%s]", imageUrl(), comment());
		}

	}

}
