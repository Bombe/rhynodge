package net.pterodactylus.rhynodge.filters.torrents;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.filters.TorrentSiteFilter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * {@link Filter} for {@code torrentz.eu} result pages.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentzEuFilter extends TorrentSiteFilter {

	@Override
	protected Elements getDataRows(Document document) {
		return document.select("div.results dl");
	}

	@Override
	protected String extractName(Element dataRow) {
		return dataRow.select("dt a").text();
	}

	@Override
	protected String extractSize(Element dataRow) {
		return dataRow.select("dd span.s").text();
	}

	@Override
	protected String extractMagnetUri(Element dataRow) {
		return null;
	}

	@Override
	protected String extractDownloadUri(Element dataRow) {
		return null;
	}

	@Override
	protected int extractFileCount(Element dataRow) {
		return 0;
	}

	@Override
	protected int extractSeedCount(Element dataRow) {
		return extractNumber(dataRow.select("dd span.u").text());
	}

	@Override
	protected int extractLeechCount(Element dataRow) {
		return extractNumber(dataRow.select("dd span.d").text());
	}

	private int extractNumber(String text) {
		if ((text == null) || text.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(text.replace(",", ""));
	}

}
