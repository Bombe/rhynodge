/*
 * Rhynodge - KickAssTorrentsFilter.java - Copyright © 2013 David Roden
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

import static com.google.common.base.Preconditions.checkState;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.TorrentState;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link Filter} implementation that parses a {@link TorrentState} from an
 * {@link HtmlState} which was generated by a {@link HttpQuery} to
 * {@code kickasstorrents.ph}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class TorrentSiteFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public State filter(@NotNull State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof HtmlState, "state is not an HtmlState but a %s", state.getClass().getName());

		/* get result table. */
		Document document = ((HtmlState) state).document();

		/* iterate over all rows. */
		Elements dataRows = getDataRows(document);
		TorrentState torrentState = new TorrentState();
		for (Element dataRow : dataRows) {
			String name = extractName(dataRow);
			String size = extractSize(dataRow);
			String magnetUri = extractMagnetUri(dataRow);
			String downloadUri = extractDownloadUri(dataRow);
			int fileCount = extractFileCount(dataRow);
			int seedCount = extractSeedCount(dataRow);
			int leechCount = extractLeechCount(dataRow);
			try {
				if ((downloadUri != null) && (downloadUri.length() > 0)) {
					downloadUri = new URI(((HtmlState) state).uri()).resolve(URLEncoder.encode(downloadUri, "UTF-8").replace("%2F", "/")).toString();
				} else {
					downloadUri = null;
				}
				TorrentFile torrentFile = new TorrentFile(name, size, magnetUri, downloadUri, fileCount, seedCount, leechCount);
				torrentState.addTorrentFile(torrentFile);
			} catch (URISyntaxException use1) {
				/* ignore; if uri was wrong, we wouldn’t be here. */
			} catch (UnsupportedEncodingException uee1) {
				/* ignore, all JVMs can do UTF-8. */
			}
		}

		return torrentState;
	}

	//
	// ABSTRACT METHODS
	//

	/**
	 * Returns the data rows from the given document.
	 *
	 * @param document
	 *            The document to get the data rows from
	 * @return The data rows
	 */
	protected abstract Elements getDataRows(Document document);

	/**
	 * Extracts the name from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the name from
	 * @return The extracted name
	 */
	protected abstract String extractName(Element dataRow);

	/**
	 * Extracts the size from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the size from
	 * @return The extracted size
	 */
	protected abstract String extractSize(Element dataRow);

	/**
	 * Extracts the magnet URI from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the magnet URI from
	 * @return The extracted magnet URI
	 */
	protected abstract String extractMagnetUri(Element dataRow);

	/**
	 * Extracts the download URI from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the download URI from
	 * @return The extracted download URI
	 */
	protected abstract String extractDownloadUri(Element dataRow);

	/**
	 * Extracts the file count from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the file count from
	 * @return The extracted file count, or {@code 0} if the file count can not
	 *         be extracted
	 */
	protected abstract int extractFileCount(Element dataRow);

	/**
	 * Extracts the seed count from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the seed count from
	 * @return The extracted seed count
	 */
	protected abstract int extractSeedCount(Element dataRow);

	/**
	 * Extracts the leech count from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the leech count from
	 * @return The extracted leech count
	 */
	protected abstract int extractLeechCount(Element dataRow);

}
