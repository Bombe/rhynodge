package net.pterodactylus.rhynodge.filters.webpages.savoy;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Information about a presentation and a link to buy a ticket.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class TicketLink {

	private final LocalDateTime presentationTime;
	private final String ticketLink;

	public static final Comparator<TicketLink> byPresentationTime = (leftTicketLink, rightTicketLink) -> leftTicketLink.getPresentationTime().compareTo(rightTicketLink.getPresentationTime());

	public TicketLink(LocalDateTime presentationTime, String ticketLink) {
		this.presentationTime = presentationTime;
		this.ticketLink = ticketLink;
	}

	public LocalDateTime getPresentationTime() {
		return presentationTime;
	}

	public String getLink() {
		return ticketLink;
	}

}
