package net.pterodactylus.rhynodge.filters.comics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pterodactylus.rhynodge.filters.ComicSiteFilter;

import com.google.common.base.Optional;
import org.jsoup.nodes.Document;

/**
 * {@link ComicSiteFilter} implementation that can parse Business Cat comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BusinessCatComicFilter extends ComicSiteFilter {

	@Override
	protected Optional<String> extractTitle(Document document) {
		String title = document.select("#comic img").attr("title");
		return title.isEmpty() ? Optional.<String>absent() : Optional.of(title);
	}

	@Override
	protected List<String> extractImageUrls(Document document) {
		return Arrays.asList(document.select("#comic img").attr("src"));
	}

	@Override
	protected List<String> extractImageComments(Document document) {
		return Collections.emptyList();
	}

}
