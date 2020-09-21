package net.pterodactylus.rhynodge.filters.webpages.savoy;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.pterodactylus.rhynodge.filters.webpages.savoy.Movie.byName;
import static net.pterodactylus.rhynodge.filters.webpages.savoy.Movie.withPresentations;
import static net.pterodactylus.rhynodge.filters.webpages.savoy.TicketLink.byPresentationTime;
import static org.jsoup.nodes.Document.createShell;
import static org.jsoup.parser.Tag.valueOf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.HtmlState;
import net.pterodactylus.rhynodge.states.OutputState;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * {@link Filter} implementation that creates a list of movies running at the
 * Savoy theatre in Hamburg.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SavoyTicketsFilter implements Filter {

	private static final MovieExtractor movieExtractor = new MovieExtractor();
	private DateTimeFormatter dateFormatter = ofPattern("dd.MM.uuuu");
	private DateTimeFormatter timeFormatter = ofPattern("HH:mm");
	private DateTimeFormatter dateTimeFormatter = ofPattern("dd.MM.uuuu, HH:mm");

	@NotNull
	@Override
	public State filter(@NotNull State state) {
		checkArgument(state instanceof HtmlState, "state must be HTML");

		HtmlState htmlState = (HtmlState) state;
		Collection<Movie> movies = movieExtractor.getMovies(htmlState.document());
		return new OutputState(getPlainTextOutput(movies), getHtmlOutput(movies));
	}

	private Optional<String> getHtmlOutput(Collection<Movie> movies) {
		Document document = createShell("");
		document.head().appendElement("style").attr("type", "text/css").text(generateStyleSheet());
		document.body().appendElement("h1").text("Kinoprogramm: Savoy");
		document.body().appendElement("h2").text("Filme");
		movies.stream().filter(withPresentations).sorted(byName).forEach(movie -> {
			document.body().appendChild(createMovieNode(movie));
		});
		document.body().appendElement("h2").text("Zeiten");
		movies.stream().flatMap(movie -> movie.getTicketLinks().stream().map(ticketLink -> new Presentation(movie, ticketLink))).sorted((leftPresentation, rightPresentation) -> leftPresentation.getTicketLink().getPresentationTime().compareTo(rightPresentation.getTicketLink().getPresentationTime())).collect(Collectors.<Presentation, LocalDate>groupingBy(presentation -> presentation.getTicketLink().getPresentationTime().toLocalDate())).entrySet().stream().sorted((leftEntry, rightEntry) -> leftEntry.getKey().compareTo(rightEntry.getKey())).forEach(dateEntry -> {
			document.body().appendChild(createDayNode(dateEntry.getKey(), dateEntry.getValue()));
		});
		document.body().appendElement("h2").text("Vorschau");
		movies.stream().filter(withPresentations.negate()).sorted(byName).forEach(movie -> {
			document.body().appendElement("div").attr("class", "name").text(movie.getName());
		});
		return of(document.toString());
	}

	private String generateStyleSheet() {
		StringBuilder styleSheet = new StringBuilder();
		styleSheet.append(".movie .name { font-weight: bold; }\n");
		styleSheet.append(".day:first-child { margin-top: 1em; }\n");
		styleSheet.append(".date { display: table-cell; font-weight: bold; }\n");
		styleSheet.append(".presentation { display: inline; }\n");
		styleSheet.append(".time, .movie { display: inline; }\n");
		return styleSheet.toString();
	}

	private Node createMovieNode(Movie movie) {
		Element movieNode = new Element(valueOf("div"), "");
		movieNode.attr("class", "movie");
		movieNode.appendElement("div").attr("class", "name").text(movie.getName());
		movie.getTicketLinks().stream().sorted(byPresentationTime).forEach(ticketLink -> {
			Element presentationNode = movieNode.appendElement("div").attr("class", "presentation");
			presentationNode.appendElement("div").attr("class", "time").text("» ").appendElement("a").attr("href", ticketLink.getLink()).text(ticketLink.getPresentationTime().format(dateTimeFormatter));
		});
		return movieNode;
	}

	private Node createDayNode(LocalDate date, List<Presentation> presentations) {
		Element dayNode = new Element(valueOf("div"), "").attr("class", "day");
		dayNode.appendElement("div").attr("class", "date").text(date.format(dateFormatter));
		presentations.stream().forEach(presentation -> {
			Element presentationNode = dayNode.appendElement("div").attr("class", "presentation");
			presentationNode.appendElement("div").attr("class", "time").text("» " + presentation.getTicketLink().getPresentationTime().format(timeFormatter));
			presentationNode.appendElement("div").attr("class", "movie").appendElement("a").attr("href", presentation.getTicketLink().getLink()).text(presentation.getMovie().getName());
		});
		return dayNode;
	}

	private Optional<String> getPlainTextOutput(Collection<Movie> movies) {
		return empty();
	}

	private static class Presentation {

		private final Movie movie;
		private final TicketLink ticketLink;

		private Presentation(Movie movie, TicketLink ticketLink) {
			this.movie = movie;
			this.ticketLink = ticketLink;
		}

		private Movie getMovie() {
			return movie;
		}

		private TicketLink getTicketLink() {
			return ticketLink;
		}

	}

}
