package net.pterodactylus.rhynodge.watchers;

import java.util.Arrays;

import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.comics.BusinessCatComicFilter;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.triggers.NewComicTrigger;

/**
 * {@link Watcher} implementation that watches for new Business Cat comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BusinessCatWatcher extends DefaultWatcher {

	public BusinessCatWatcher() {
		super(new HttpQuery("https://www.businesscatcomic.com/"), Arrays.asList(new HtmlFilter(), new BusinessCatComicFilter()), new NewComicTrigger());
	}

}
