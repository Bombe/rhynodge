package net.pterodactylus.rhynodge.filters.comics;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Loads a resource from the classpath and parses it as HTML.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ComicLoader {

	static Document loadDocument(String resourceName, String baseUri) throws IOException {
		InputStream inputStream = ComicLoader.class.getResourceAsStream(resourceName);
		Document document = Jsoup.parse(inputStream, "UTF-8", baseUri);
		return document;
	}

}
