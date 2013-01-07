/*
 * Reactor - NewTorrentTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.triggers;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import net.pterodactylus.reactor.Reaction;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.output.DefaultOutput;
import net.pterodactylus.reactor.output.Output;
import net.pterodactylus.reactor.states.TorrentState;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Lists;

/**
 * {@link Trigger} implementation that is triggered by {@link TorrentFile}s that
 * appear in the current {@link TorrentState} but not in the previous one.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewTorrentTrigger implements Trigger {

	/** The newly detected torrent files. */
	private List<TorrentFile> torrentFiles = Lists.newArrayList();

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(State currentState, State previousState) {
		checkState(currentState instanceof TorrentState, "currentState is not a TorrentState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof TorrentState, "previousState is not a TorrentState but a %s", currentState.getClass().getName());
		TorrentState currentTorrentState = (TorrentState) currentState;
		TorrentState previousTorrentState = (TorrentState) previousState;
		torrentFiles.clear();
		for (TorrentFile torrentFile : currentTorrentState) {
			torrentFiles.add(torrentFile);
		}
		for (TorrentFile torrentFile : previousTorrentState) {
			torrentFiles.remove(torrentFile);
		}
		return !torrentFiles.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Output output(Reaction reaction) {
		DefaultOutput output = new DefaultOutput(String.format("Found %d new Torrent(s)!", torrentFiles.size()));
		output.addText("text/plain", getPlainTextList(torrentFiles));
		output.addText("text/html", getHtmlTextList(torrentFiles));
		return output;
	}

	//
	// STATIC METHODS
	//

	/**
	 * Generates a plain text list of torrent files.
	 *
	 * @param torrentFiles
	 *            The torrent files to list
	 * @return The generated plain text
	 */
	private static String getPlainTextList(List<TorrentFile> torrentFiles) {
		StringBuilder plainText = new StringBuilder();
		plainText.append("New Torrents:\n\n");
		for (TorrentFile torrentFile : torrentFiles) {
			plainText.append(torrentFile.name()).append('\n');
			plainText.append('\t').append(torrentFile.size()).append(" in ").append(torrentFile.fileCount()).append(" file(s)\n");
			plainText.append('\t').append(torrentFile.seedCount()).append(" seed(s), ").append(torrentFile.leechCount()).append(" leecher(s)\n");
			plainText.append('\t').append(torrentFile.magnetUri()).append('\n');
			plainText.append('\t').append(torrentFile.downloadUri()).append('\n');
			plainText.append('\n');
		}
		return plainText.toString();
	}

	/**
	 * Generates an HTML list of the given torrent files.
	 *
	 * @param torrentFiles
	 *            The torrent files to list
	 * @return The generated HTML
	 */
	private static String getHtmlTextList(List<TorrentFile> torrentFiles) {
		StringBuilder htmlText = new StringBuilder();
		htmlText.append("<html><body>\n");
		htmlText.append("<h1>New Torrents</h1>\n");
		htmlText.append("<ul>\n");
		for (TorrentFile torrentFile : torrentFiles) {
			htmlText.append("<li><strong>").append(StringEscapeUtils.escapeHtml4(torrentFile.name())).append("</strong></li>");
			htmlText.append("<div>Size: <strong>").append(StringEscapeUtils.escapeHtml4(torrentFile.size())).append("</strong> in <strong>").append(torrentFile.fileCount()).append("</strong> file(s)</div>");
			htmlText.append("<div><strong>").append(torrentFile.seedCount()).append("</strong> seed(s), <strong>").append(torrentFile.leechCount()).append("</strong> leecher(s)</div>");
			htmlText.append(String.format("<div><a href=\"%s\">Magnet URI</a></div>", StringEscapeUtils.escapeHtml4(torrentFile.magnetUri())));
			htmlText.append(String.format("<div><a href=\"%s\">Download URI</a></div>", StringEscapeUtils.escapeHtml4(torrentFile.downloadUri())));
		}
		htmlText.append("</ul>\n");
		htmlText.append("</body></html>\n");
		return htmlText.toString();
	}

}
