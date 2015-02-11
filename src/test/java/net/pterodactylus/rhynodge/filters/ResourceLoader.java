package net.pterodactylus.rhynodge.filters;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Loads a resource from the classpath and parses it as HTML.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ResourceLoader {

	public static Document loadDocument(Class<?> classToLoadFrom, String resourceName, String baseUri) throws IOException {
		InputStream inputStream = classToLoadFrom.getResourceAsStream(resourceName);
		Document document = Jsoup.parse(inputStream, "UTF-8", baseUri);
		return document;
	}

}
