/*
 * Rhynodge - PirateBayFilter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.filters.torrents;

import java.util.regex.Pattern;

import net.pterodactylus.rhynodge.filters.TorrentSiteFilter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link net.pterodactylus.rhynodge.filters.TorrentSiteFilter} implementation that can parse
 * {@code thepiratebay.se} result pages.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class PirateBayFilter extends TorrentSiteFilter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Elements getDataRows(Document document) {
		return document.select("table#searchResult tbody tr:has(.vertTh)");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractName(Element dataRow) {
		return dataRow.select(".detName a").text();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractSize(Element dataRow) {
		return dataRow.select(".detDesc").text().split(Pattern.quote(","))[1].split(Pattern.quote("Size"))[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractMagnetUri(Element dataRow) {
		return dataRow.select("a[href^=magnet:]").attr("href");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String extractDownloadUri(Element dataRow) {
		return dataRow.select("a[href^=//torrents.]").attr("href");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractFileCount(Element dataRow) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractSeedCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("td:eq(2)").text());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int extractLeechCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("td:eq(3)").text());
	}

}
