package net.pterodactylus.rhynodge.filters.webpages.savoy;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Information about a movie.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Movie {

	private final String name;
	private final List<TicketLink> ticketLinks = new ArrayList<>();

	public static final Predicate<Movie> withPresentations = movie -> !movie.getTicketLinks().isEmpty();
	public static final Comparator<Movie> byName = (leftMovie, rightMovie) -> leftMovie.getName().compareToIgnoreCase(rightMovie.getName());

	public Movie(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<TicketLink> getTicketLinks() {
		return ticketLinks;
	}

	public void addTicketLink(TicketLink ticketLink) {
		ticketLinks.add(ticketLink);
	}

	@Override
	public String toString() {
		return format("%s (%d)", name, ticketLinks.size());
	}

}
