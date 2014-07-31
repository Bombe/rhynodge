package net.pterodactylus.rhynodge.filters.webpages.savoy;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Extracts {@link Movie} information from an HTML document.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MovieExtractor {

	private static final Pattern datePattern = Pattern.compile(".*([0-9]{2}\\.[0-9]{2}\\.[0-9]{2}).*");
	private static final Pattern timePattern = Pattern.compile(".*([0-9]{2}:[0-9]{2}).*");
	private static final DateTimeFormatter dateFormatter = ofPattern("dd.MM.uu");
	private static final DateTimeFormatter timeFormatter = ofPattern("HH:mm");

	public Set<Movie> getMovies(Document document) {
		Set<Movie> movies = new HashSet<>();
		for (Element movieElement : document.select(".tx-spmovies-pi1-listrow")) {
			String name = movieElement.select(".tx-spmovies-pi1-header h1").text();
			Movie movie = new Movie(name);
			for (TicketLink ticketLink : extractTicketLinks(movieElement)) {
				movie.addTicketLink(ticketLink);
			}
			movies.add(movie);
		}
		return movies;
	}

	private Iterable<? extends TicketLink> extractTicketLinks(Element movieElement) {
		Set<TicketLink> ticketLinks = new HashSet<>();
		int dateCellIndex = 1;
		for (Element dateCell : movieElement.select(".tx-spmovies-pi1-date-column")) {
			Optional<String> dateString = extractDateString(dateCell);
			if (!dateString.isPresent()) {
				continue;
			}
			for (Element timeCell : getTimeCells(movieElement, dateCellIndex++)) {
				Optional<String> timeString = extractTimeString(timeCell.select("a").text());
				if (!timeString.isPresent()) {
					continue;
				}
				LocalDateTime localDateTime = getPresentationTime(dateString, timeString);
				String link = timeCell.select("a").attr("href");
				TicketLink ticketLink = new TicketLink(localDateTime, link);
				ticketLinks.add(ticketLink);
			}
		}
		return ticketLinks;
	}

	private LocalDateTime getPresentationTime(Optional<String> dateString, Optional<String> timeString) {
		LocalDate date = LocalDate.parse(dateString.get(), dateFormatter);
		LocalTime localTime = LocalTime.parse(timeString.get(), timeFormatter);
		return date.atTime(localTime);
	}

	private Optional<String> extractTimeString(String cellContent) {
		Matcher timeMatcher = timePattern.matcher(cellContent);
		if (!timeMatcher.matches() || timeMatcher.groupCount() < 1) {
			return empty();
		}
		return of(timeMatcher.group(1));
	}

	private Iterable<? extends Element> getTimeCells(Element movieElement, int dateCellIndex) {
		return movieElement.select(".tx-spmovies-pi1-shows:eq(" + dateCellIndex + ") div.time");
	}

	private Optional<String> extractDateString(Element dateCell) {
		Matcher matcher = datePattern.matcher(dateCell.text());
		if (!matcher.matches() || matcher.groupCount() < 1) {
			return empty();
		}
		return of(matcher.group(1));
	}

}
