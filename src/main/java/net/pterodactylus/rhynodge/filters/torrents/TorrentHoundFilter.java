package net.pterodactylus.rhynodge.filters.torrents;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import net.pterodactylus.rhynodge.filters.TorrentSiteFilter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * {@link TorrentSiteFilter} implementation that can parse TorrentHound
 * results.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TorrentHoundFilter extends TorrentSiteFilter {

	@Override
	protected Elements getDataRows(Document document) {
		return document.select("table.searchtable:gt(0) tr:gt(0)");
	}

	@Override
	protected String extractName(Element dataRow) {
		return dataRow.select("td > a").stream().map(element -> {
			AtomicBoolean foundBr = new AtomicBoolean(false);
			List<Node> nodes = element.childNodes().stream().filter(textNode -> {
				if (textNode.nodeName().equals("br")) {
					foundBr.set(true);
				}
				return !foundBr.get();
			}).collect(Collectors.toList());
			Element e = new Element(Tag.valueOf("span"), "");
			nodes.stream().forEach(node -> e.appendChild(node));
			return e.text();
		}).findFirst().get();
	}

	@Override
	protected String extractSize(Element dataRow) {
		return dataRow.select("span.size").text();
	}

	@Override
	protected String extractMagnetUri(Element dataRow) {
		return dataRow.select("div.sfloat a[title=Magnet download]").attr("href");
	}

	@Override
	protected String extractDownloadUri(Element dataRow) {
		return dataRow.select("div.sfloat a[title=.torrent download]").attr("href");
	}

	@Override
	protected int extractFileCount(Element dataRow) {
		return 0;
	}

	@Override
	protected int extractSeedCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("span.seeds").text().replaceAll("[^0-9]", ""));
	}

	@Override
	protected int extractLeechCount(Element dataRow) {
		return Integer.valueOf(dataRow.select("span.leeches").text().replaceAll("[^0-9]", ""));
	}

}
