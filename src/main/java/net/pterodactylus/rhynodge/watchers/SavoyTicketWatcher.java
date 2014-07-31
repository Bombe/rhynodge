package net.pterodactylus.rhynodge.watchers;

import static java.util.Arrays.asList;

import net.pterodactylus.rhynodge.Watcher;
import net.pterodactylus.rhynodge.filters.HtmlFilter;
import net.pterodactylus.rhynodge.filters.webpages.SavoyTicketsFilter;
import net.pterodactylus.rhynodge.queries.HttpQuery;
import net.pterodactylus.rhynodge.triggers.AlwaysTrigger;

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
				new AlwaysTrigger()
		);
	}

}
