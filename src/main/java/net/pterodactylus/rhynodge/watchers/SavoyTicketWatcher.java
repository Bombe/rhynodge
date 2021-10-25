package net.pterodactylus.rhynodge.watchers;

import static java.util.Arrays.asList;

import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.webpages.savoy.SavoyTicketsFilter;
import net.pterodactylus.rhynodge.mergers.LastStateMerger;
import net.pterodactylus.rhynodge.queries.HttpQuery;

/**
 * {@link Watcher} implementation that shows tickets sold in the Savoy theatre.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SavoyTicketWatcher extends DefaultWatcher {

	public SavoyTicketWatcher() {
		super(
				new HttpQuery("http://www.savoy-filmtheater.de/filmprogramm.html"),
				asList(
						new HtmlFilter(),
						new SavoyTicketsFilter()
				),
				new LastStateMerger()
		);
	}

}
