/*
 * Reactor - KickAssTorrentsFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.filters;

import static com.google.common.base.Preconditions.checkState;

import java.net.URI;
import java.net.URISyntaxException;

import net.pterodactylus.reactor.Filter;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.queries.HttpQuery;
import net.pterodactylus.reactor.states.FailedState;
import net.pterodactylus.reactor.states.HtmlState;
import net.pterodactylus.reactor.states.TorrentState;
import net.pterodactylus.reactor.states.TorrentState.TorrentFile;

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
public class KickAssTorrentsFilter implements Filter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State filter(State state) {
		if (!state.success()) {
			return FailedState.from(state);
		}
		checkState(state instanceof HtmlState, "state is not an HtmlState but a %s", state.getClass().getName());

		/* get result table. */
		Document document = ((HtmlState) state).document();
		Elements mainTable = document.select("table.data");
		if (mainTable.isEmpty()) {
			/* no main table? */
			return new FailedState();
		}

		/* iterate over all rows. */
		TorrentState torrentState = new TorrentState();
		Elements dataRows = mainTable.select("tr:gt(0)");
		for (Element dataRow : dataRows) {
			String name = extractName(dataRow);
			String size = extractSize(dataRow);
			String magnetUri = extractMagnetUri(dataRow);
			String downloadUri;
			try {
				downloadUri = new URI(((HtmlState) state).uri()).resolve(extractDownloadUri(dataRow)).toString();
				TorrentFile torrentFile = new TorrentFile(name, size, magnetUri, downloadUri);
				torrentState.addTorrentFile(torrentFile);
			} catch (URISyntaxException use1) {
				/* ignore; if uri was wrong, we wouldn’t be here. */
			}
		}

		return torrentState;
	}

	//
	// STATIC METHODS
	//

	/**
	 * Extracts the name from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the name from
	 * @return The extracted name
	 */
	private static String extractName(Element dataRow) {
		return dataRow.select("div.torrentname a.normalgrey").text();
	}

	/**
	 * Extracts the size from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the size from
	 * @return The extracted size
	 */
	private static String extractSize(Element dataRow) {
		return dataRow.select("td:eq(1)").text();
	}

	/**
	 * Extracts the magnet URI from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the magnet URI from
	 * @return The extracted magnet URI
	 */
	private static String extractMagnetUri(Element dataRow) {
		return dataRow.select("a.imagnet").attr("href");
	}

	/**
	 * Extracts the download URI from the given row.
	 *
	 * @param dataRow
	 *            The row to extract the download URI from
	 * @return The extracted download URI
	 */
	private static String extractDownloadUri(Element dataRow) {
		return dataRow.select("a.idownload:not(.partner1Button)").attr("href");
	}

}