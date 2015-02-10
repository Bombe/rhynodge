package net.pterodactylus.rhynodge.filters.comics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;

/**
 * {@link ComicSiteFilter} implementation that can parse “Chief O’Brien at Work”
 * comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ChiefOBrienAtWorkComicFilter extends ComicSiteFilter {

	@Override
	protected List<String> extractImageUrls(Document document) {
		return Arrays.asList(document.select(".P .P-H .larger-width img").get(0).attr("src"));
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

	@Override
	protected Optional<String> extractTitle(Document document) {
		return Optional.fromNullable(document.select(".P-post .captions p").get(0).text());
	}

}
