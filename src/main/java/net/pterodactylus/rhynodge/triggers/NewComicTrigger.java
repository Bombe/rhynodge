/*
 * rhynodge - NewComicTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.triggers;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;
import net.pterodactylus.rhynodge.states.ComicState.Strip;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link Trigger} implementation that detects the presence of new {@link
 * Comic}s in a {@link ComicState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewComicTrigger implements Trigger {

	/** The new comics. */
	private final List<Comic> newComics = Lists.newArrayList();

	/** The latest comic state. */
	private ComicState mergedComicState;

	@Override
	public State mergeStates(State previousState, State currentState) {
		checkArgument(previousState instanceof ComicState, "previous state must be a comic state");
		checkArgument(currentState instanceof ComicState, "current state must be a comic state");

		ComicState previousComicState = (ComicState) previousState;
		ComicState currentComicState = (ComicState) currentState;

		/* copy old state into new state. */
		mergedComicState = new ComicState();
		for (Comic comic : previousComicState) {
			mergedComicState.add(comic);
		}

		newComics.clear();
		for (Comic comic : currentComicState) {
			if (!mergedComicState.comics().contains(comic)) {
				newComics.add(comic);
				mergedComicState.add(comic);
			}
		}

		return mergedComicState;
	}

	@Override
	public boolean triggers() {
		return !newComics.isEmpty();
	}

	@Override
	public Output output(Reaction reaction) {
		DefaultOutput output = new DefaultOutput(String.format("New Comic found for “%s!”", reaction.name()));

		output.addText("text/plain", generatePlainText());
		output.addText("text/html", generateHtmlText());

		return output;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Generates a list of the new comics in plain text format.
	 *
	 * @return The list of new comics in plain text format
	 */
	private String generatePlainText() {
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

	/**
	 * Generates a list of new comics in HTML format.
	 *
	 * @return The list of new comics in HTML format
	 */
	private String generateHtmlText() {
		StringBuilder html = new StringBuilder();
		html.append("<body>");

		for (Comic newComic : newComics) {
			generateComicHtml(html, newComic);
		}

		List<Comic> latestComics = new ArrayList<Comic>(mergedComicState.comics());
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

	/**
	 * Generates the HTML for a single comic.
	 *
	 * @param html
	 * 		The string builder to append the HTML to
	 * @param comic
	 * 		The comic to render
	 */
	private void generateComicHtml(StringBuilder html, Comic comic) {
		html.append("<h1>").append(StringEscapeUtils.escapeHtml4(comic.title())).append("</h1>\n");
		for (Strip strip : comic) {
			html.append("<div><img src=\"").append(StringEscapeUtils.escapeHtml4(strip.imageUrl()));
			html.append("\" alt=\"").append(StringEscapeUtils.escapeHtml4(strip.comment()));
			html.append("\" title=\"").append(StringEscapeUtils.escapeHtml4(strip.comment()));
			html.append("\"></div>\n");
		}
	}

}
