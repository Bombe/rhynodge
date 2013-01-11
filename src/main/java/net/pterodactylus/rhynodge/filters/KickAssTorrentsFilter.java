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

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.TorrentState;

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
public class KickAssTorrentsFilter extends TorrentSiteFilter {

	//
	// TORRENTSITEFILTER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Elements getDataRows(Document document) {
		return document.select("table.data").select("tr:gt(0)");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractName(Element dataRow) {
		return dataRow.select("div.torrentname a.normalgrey").text();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractSize(Element dataRow) {
		return dataRow.select("td:eq(1)").text();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractMagnetUri(Element dataRow) {
		return dataRow.select("a.imagnet").attr("href");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractDownloadUri(Element dataRow) {
		return dataRow.select("a.idownload:not(.partner1Button)").attr("href");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractFileCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("td:eq(2)").text());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractSeedCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("td:eq(4)").text());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractLeechCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("td:eq(5)").text());
	}

}