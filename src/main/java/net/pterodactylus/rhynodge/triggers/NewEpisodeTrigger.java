/*
 * Rhynodge - NewEpisodeTrigger.java - Copyright © 2013 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.rhynodge.triggers;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;
import net.pterodactylus.rhynodge.states.EpisodeState;
import net.pterodactylus.rhynodge.states.EpisodeState.Episode;
import net.pterodactylus.rhynodge.states.TorrentState.TorrentFile;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * {@link Trigger} implementation that compares two {@link EpisodeState}s for
 * new and changed {@link Episode}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewEpisodeTrigger implements Trigger {

	/** All episodes. */
	private final Collection<Episode> allEpisodes = Sets.newHashSet();

	/** All new episodes. */
	private final Collection<Episode> newEpisodes = Sets.newHashSet();

	/** All changed episodes. */
	private final Collection<Episode> changedEpisodes = Sets.newHashSet();

	/** All new torrent files. */
	private final Collection<TorrentFile> newTorrentFiles = Sets.newHashSet();

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDocs}
	 */
	@Override
	public State mergeStates(State previousState, State currentState) {
		checkState(currentState instanceof EpisodeState, "currentState is not a EpisodeState but a %s", currentState.getClass().getName());
		checkState(previousState instanceof EpisodeState, "previousState is not a EpisodeState but a %s", currentState.getClass().getName());
		newEpisodes.clear();
		changedEpisodes.clear();
		this.allEpisodes.clear();
		newTorrentFiles.clear();
		Map<Episode, Episode> allEpisodes = Maps.newHashMap(FluentIterable.from(((EpisodeState) previousState).episodes()).toMap(new Function<Episode, Episode>() {

			@Override
			public Episode apply(Episode episode) {
				return episode;
			}
		}));
		for (Episode episode : ((EpisodeState) currentState).episodes()) {
			if (!allEpisodes.containsKey(episode)) {
				allEpisodes.put(episode, episode);
				newEpisodes.add(episode);
			}
			Episode existingEpisode = allEpisodes.get(episode);
			for (TorrentFile torrentFile : Lists.newArrayList(episode.torrentFiles())) {
				int oldSize = existingEpisode.torrentFiles().size();
				existingEpisode.addTorrentFile(torrentFile);
				int newSize = existingEpisode.torrentFiles().size();
				if (oldSize != newSize) {
					newTorrentFiles.add(torrentFile);
				}
				if (!newEpisodes.contains(existingEpisode) && (oldSize != newSize)) {
					changedEpisodes.add(existingEpisode);
				}
			}
		}
		this.allEpisodes.addAll(allEpisodes.values());
		return new EpisodeState(this.allEpisodes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers() {
		return !newEpisodes.isEmpty() || !changedEpisodes.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Output output(Reaction reaction) {
		String summary;
		if (!newEpisodes.isEmpty()) {
			if (!changedEpisodes.isEmpty()) {
				summary = String.format("%d new and %d changed Torrent(s) for “%s!”", newEpisodes.size(), changedEpisodes.size(), reaction.name());
			} else {
				summary = String.format("%d new Torrent(s) for “%s!”", newEpisodes.size(), reaction.name());
			}
		} else {
			summary = String.format("%d changed Torrent(s) for “%s!”", changedEpisodes.size(), reaction.name());
		}
		DefaultOutput output = new DefaultOutput(summary);
		output.addText("text/plain", generatePlainText(reaction));
		output.addText("text/html", generateHtmlText(reaction));
		return output;
	}

	//
	// STATIC METHODS
	//

	/**
	 * Generates the plain text trigger output.
	 *
	 * @param reaction
	 *            The reaction that was triggered
	 * @return The plain text output
	 */
	private String generatePlainText(Reaction reaction) {
		StringBuilder stringBuilder = new StringBuilder();
		if (!newEpisodes.isEmpty()) {
			stringBuilder.append(reaction.name()).append(" - New Episodes\n\n");
			for (Episode episode : newEpisodes) {
				stringBuilder.append("- ").append(episode.identifier()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("  - ").append(torrentFile.name()).append(", ").append(torrentFile.size()).append("\n");
					if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
						stringBuilder.append("    Magnet: ").append(torrentFile.magnetUri()).append("\n");
					}
					if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
						stringBuilder.append("    Download: ").append(torrentFile.downloadUri()).append("\n");
					}
				}
			}
		}
		if (!changedEpisodes.isEmpty()) {
			stringBuilder.append(reaction.name()).append(" - Changed Episodes\n\n");
			for (Episode episode : changedEpisodes) {
				stringBuilder.append("- ").append(episode.identifier()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("  - ").append(torrentFile.name()).append(", ").append(torrentFile.size()).append("\n");
					if ((torrentFile.magnetUri() != null) && (torrentFile.magnetUri().length() > 0)) {
						stringBuilder.append("    Magnet: ").append(torrentFile.magnetUri()).append("\n");
					}
					if ((torrentFile.downloadUri() != null) && (torrentFile.downloadUri().length() > 0)) {
						stringBuilder.append("    Download: ").append(torrentFile.downloadUri()).append("\n");
					}
				}
			}
		}
		/* list all known episodes. */
		stringBuilder.append(reaction.name()).append(" - All Known Episodes\n\n");
		ImmutableMap<Integer, Collection<Episode>> episodesBySeason = FluentIterable.from(allEpisodes).index(new Function<Episode, Integer>() {

			@Override
			public Integer apply(Episode episode) {
				return episode.season();
			}
		}).asMap();
		for (Entry<Integer, Collection<Episode>> seasonEntry : episodesBySeason.entrySet()) {
			stringBuilder.append("  Season ").append(seasonEntry.getKey()).append("\n\n");
			for (Episode episode : Ordering.natural().sortedCopy(seasonEntry.getValue())) {
				stringBuilder.append("    Episode ").append(episode.episode()).append("\n");
				for (TorrentFile torrentFile : episode) {
					stringBuilder.append("      Size: ").append(torrentFile.size());
					stringBuilder.append(" in ").append(torrentFile.fileCount()).append(" file(s): ");
					stringBuilder.append(torrentFile.magnetUri());
				}
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Generates the HTML trigger output.
	 *
	 * @param reaction
	 *            The reaction that was triggered
	 * @return The HTML output
	 */
	private String generateHtmlText(Reaction reaction) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>\n");
		/* show all known episodes. */
		htmlBuilder.append("<table>\n<caption>All Known Episodes</caption>\n");
		htmlBuilder.append("<thead>\n");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<th>Season</th>");
		htmlBuilder.append("<th>Episode</th>");
		htmlBuilder.append("<th>Filename</th>");
		htmlBuilder.append("<th>Size</th>");
		htmlBuilder.append("<th>File(s)</th>");
		htmlBuilder.append("<th>Seeds</th>");
		htmlBuilder.append("<th>Leechers</th>");
		htmlBuilder.append("<th>Magnet</th>");
		htmlBuilder.append("<th>Download</th>");
		htmlBuilder.append("</tr>\n");
		htmlBuilder.append("</thead>\n");
		htmlBuilder.append("<tbody>\n");
		Episode lastEpisode = null;
		for (Entry<Integer, Collection<Episode>> seasonEntry : FluentIterable.from(Ordering.natural().reverse().sortedCopy(allEpisodes)).index(Episode.BY_SEASON).asMap().entrySet()) {
			for (Episode episode : seasonEntry.getValue()) {
				for (TorrentFile torrentFile : episode) {
					if (newEpisodes.contains(episode)) {
						htmlBuilder.append("<tr style=\"background-color: #eeffee;\">");
					} else if (newTorrentFiles.contains(torrentFile)) {
						htmlBuilder.append("<tr style=\"color: #008000;\">");
					} else {
						htmlBuilder.append("<tr>");
					}
					if ((lastEpisode == null) || !lastEpisode.equals(episode)) {
						htmlBuilder.append("<td>").append(episode.season()).append("</td><td>").append(episode.episode()).append("</td>");
					} else {
						htmlBuilder.append("<td colspan=\"2\"></td>");
					}
					htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.name())).append("</td>");
					htmlBuilder.append("<td>").append(StringEscapeUtils.escapeHtml4(torrentFile.size())).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.fileCount()).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.seedCount()).append("</td>");
					htmlBuilder.append("<td>").append(torrentFile.leechCount()).append("</td>");
					htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.magnetUri())).append("\">Link</a></td>");
					htmlBuilder.append("<td><a href=\"").append(StringEscapeUtils.escapeHtml4(torrentFile.downloadUri())).append("\">Link</a></td>");
					htmlBuilder.append("</tr>\n");
					lastEpisode = episode;
				}
			}
		}
		htmlBuilder.append("</tbody>\n");
		htmlBuilder.append("</table>\n");
		htmlBuilder.append("</body></html>\n");
		return htmlBuilder.toString();
	}

}
