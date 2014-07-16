package net.pterodactylus.rhynodge.watchers;

import static java.util.Arrays.asList;

import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.comics.HeldentageFilter;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.triggers.NewComicTrigger;

/**
 * {@link Watcher} implementation that watches for new “Heldentage” comics.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HeldentageWatcher extends DefaultWatcher {

	public HeldentageWatcher(Query query, List<Filter> filters, Trigger trigger) {
		super(new HttpQuery("http://www.der-flix.de/"), asList(new HtmlFilter(), new HeldentageFilter()), new NewComicTrigger());
	}

}
