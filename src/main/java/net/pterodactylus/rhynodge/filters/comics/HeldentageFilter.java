package net.pterodactylus.rhynodge.filters.comics;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;

/**
 * {@link ComicSiteFilter} implementation that can parse “Heldentage” comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HeldentageFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		return extractImageUrls(document).isEmpty() ? absent() : of("");
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		return document.select("#mod_jheldentage_display_latest img").stream().map(element -> element.attr("src")).collect(toList());
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return emptyList();
	}

}
