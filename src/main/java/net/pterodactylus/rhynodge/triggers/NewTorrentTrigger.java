/*
 * Rhynodge - NewTorrentTrigger.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.Set;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * {@link Trigger} implementation that is triggered by {@link TorrentFile}s that
 * appear in the current {@link TorrentState} but not in the previous one.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewTorrentTrigger implements Trigger {

	/** All known torrents. */
	private final Set<TorrentFile> allTorrentFiles = Sets.newHashSet();

	/** The newly detected torrent files. */
	private final List<TorrentFile> newTorrentFiles = Lists.newArrayList();

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDocs}
	 */
	@Override
	public State mergeStates(State previousState, State currentState) {
		checkState(currentState instanceof TorrentState, "currentState is not a TorrentState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof TorrentState, "previousState is not a TorrentState but a %s", currentState.getClass().getName());
		TorrentState currentTorrentState = (TorrentState) currentState;
		TorrentState previousTorrentState = (TorrentState) previousState;

		allTorrentFiles.clear();
		newTorrentFiles.clear();
		allTorrentFiles.addAll(previousTorrentState.torrentFiles());
		for (TorrentFile torrentFile : currentTorrentState) {
			if (allTorrentFiles.add(torrentFile)) {
				newTorrentFiles.add(torrentFile);
			}
		}

		return new TorrentState(allTorrentFiles);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers() {
		return !newTorrentFiles.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Output output(Reaction reaction) {
		DefaultOutput output = new DefaultOutput(String.format("Found %d new Torrent(s) for “%s!”", newTorrentFiles.size(), reaction.name()));
		output.addText("text/plain", getPlainTextList(reaction));
		output.addText("text/html", getHtmlTextList(reaction));
		return output;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Generates a plain text list of torrent files.
	 *
	 * @param reaction
	 *            The reaction that was triggered
	 * @return The generated plain text
	 */
	private String getPlainTextList(Reaction reaction) {
		StringBuilder plainText = new StringBuilder();
		plainText.append("New Torrents:\n\n");
		for (TorrentFile torrentFile : newTorrentFiles) {
			plainText.append(torrentFile.name()).append('\n');
			plainText.append('\t').append(torrentFile.size()).append(" in ").append(torrentFile.fileCount()).append(" file(s)\n");
			plainText.append('\t').append(torrentFile.seedCount()).append(" seed(s), ").append(torrentFile.leechCount()).append(" leecher(s)\n");
			if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
				plainText.append('\t').append(torrentFile.magnetUri()).append('\n');
			}
			if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
				plainText.append('\t').append(torrentFile.downloadUri()).append('\n');
			}
			plainText.append('\n');
		}
		return plainText.toString();
	}

	/**
	 * Generates an HTML list of the given torrent files.
	 *
	 * @param reaction
	 *            The reaction that was triggered
	 * @return The generated HTML
	 */
	private String getHtmlTextList(Reaction reaction) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>\n");
		htmlBuilder.append("<table>\n<caption>All Known Torrents</caption>\n");
		htmlBuilder.append("<thead>\n");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<th>Filename</th>");
		htmlBuilder.append("<th>Size</th>");
		htmlBuilder.append("<th>File(s)</th>");
		htmlBuilder.append("<th>Seeds</th>");
		htmlBuilder.append("<th>Leechers</th>");
		htmlBuilder.append("<th>Magnet</th>");
		htmlBuilder.append("<th>Download</th>");
		htmlBuilder.append("</tr>\n");
		htmlBuilder.append("</thead>\n");
		htmlBuilder.append("<tbody>\n");
		for (TorrentFile torrentFile : allTorrentFiles) {
			if (newTorrentFiles.contains(torrentFile)) {
				htmlBuilder.append("<tr style=\"color: #008000; font-weight: bold;\">");
			} else {
				htmlBuilder.append("<tr>");
			}
			htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.name())).append("</td>");
			htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.size())).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.fileCount()).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.seedCount()).append("</td>");
			htmlBuilder.append("<td>").append(torrentFile.leechCount()).append("</td>");
			htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.magnetUri())).append("\">Link</a></td>");
			htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.downloadUri())).append("\">Link</a></td>");
			htmlBuilder.append("</tr>\n");
		}
		htmlBuilder.append("</tbody>\n");
		htmlBuilder.append("</table>\n");
		htmlBuilder.append("</body></html>\n");
		return htmlBuilder.toString();
	}

}
