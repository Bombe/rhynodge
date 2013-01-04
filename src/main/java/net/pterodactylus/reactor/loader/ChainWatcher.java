/*
 * Reactor - ChainWatcher.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.pterodactylus.reactor.Reaction;
import net.pterodactylus.reactor.engine.Engine;
import net.pterodactylus.reactor.loader.Chain.Parameter;
import net.pterodactylus.reactor.loader.Chain.Part;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Uninterruptibles;

/**
 * Watches a directory for chain XML files and loads and unloads
 * {@link Reaction}s from the {@link Engine}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ChainWatcher extends AbstractExecutionThreadService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(ChainWatcher.class);

	/** The reaction loader. */
	private final ReactionLoader reactionLoader = new ReactionLoader();

	/** The engine to load reactions with. */
	private final Engine engine;

	/** The directory to watch for chain XML files. */
	private final String directory;

	/**
	 * Creates a new chain watcher.
	 *
	 * @param engine
	 *            The engine to load reactions with
	 * @param directory
	 *            The directory to watch
	 */
	public ChainWatcher(Engine engine, String directory) {
		this.engine = engine;
		this.directory = directory;
	}

	//
	// ABSTRACTEXECUTIONTHREADSERVICE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void run() throws Exception {

		/* loaded chains. */
		final Map<String, Chain> loadedChains = new HashMap<String, Chain>();

		while (isRunning()) {

			/* check if directory is there. */
			File directoryFile = new File(directory);
			if (!directoryFile.exists() || !directoryFile.isDirectory() || !directoryFile.canRead()) {
				Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
				continue;
			}

			/* list all files, scan for XMLs. */
			logger.debug(String.format("Scanning %s...", directory));
			File[] xmlFiles = directoryFile.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			logger.debug(String.format("Found %d XML file(s), parsing...", xmlFiles.length));

			/* now parse all XML files. */
			Map<String, Chain> chains = new HashMap<String, Chain>();
			for (File xmlFile : xmlFiles) {
				Serializer serializer = new Persister();
				logger.debug(String.format("Reading %s...", xmlFile.getPath()));
				Chain chain = serializer.read(Chain.class, xmlFile);

				/* dump chain */
				logger.debug(String.format(" Enabled: %s", chain.enabled()));

				logger.debug(String.format(" Query: %s", chain.query().name()));
				for (Parameter parameter : chain.query().parameters()) {
					logger.debug(String.format("  Parameter: %s=%s", parameter.name(), parameter.value()));
				}
				for (Part filter : chain.filters()) {
					logger.debug(String.format(" Filter: %s", filter.name()));
					for (Parameter parameter : filter.parameters()) {
						logger.debug(String.format("  Parameter: %s=%s", parameter.name(), parameter.value()));
					}
				}
				logger.debug(String.format(" Trigger: %s", chain.trigger().name()));
				for (Parameter parameter : chain.trigger().parameters()) {
					logger.debug(String.format("  Parameter: %s=%s", parameter.name(), parameter.value()));
				}
				logger.debug(String.format(" Action: %s", chain.action().name()));
				for (Parameter parameter : chain.action().parameters()) {
					logger.debug(String.format("  Parameter: %s=%s", parameter.name(), parameter.value()));
				}

				chains.put(xmlFile.getName(), chain);
			}

			/* filter enabled chains. */
			Map<String, Chain> enabledChains = Maps.filterEntries(chains, new Predicate<Entry<String, Chain>>() {

				@Override
				public boolean apply(Entry<String, Chain> chainEntry) {
					return chainEntry.getValue().enabled();
				}
			});
			logger.debug(String.format("Found %d enabled Chain(s).", enabledChains.size()));

			/* check for removed chains. */
			for (Entry<String, Chain> loadedChain : loadedChains.entrySet()) {

				/* skip chains that still exist. */
				if (enabledChains.containsKey(loadedChain.getKey())) {
					continue;
				}

				logger.info(String.format("Removing Chain: %s", loadedChain.getKey()));
				engine.removeReaction(loadedChain.getKey());
				loadedChains.remove(loadedChain.getKey());
			}

			/* check for new chains. */
			for (Entry<String, Chain> enabledChain : enabledChains.entrySet()) {

				/* skip already loaded chains. */
				if (loadedChains.containsValue(enabledChain.getValue())) {
					continue;
				}

				logger.info(String.format("Loading new Chain: %s", enabledChain.getKey()));

				Reaction reaction = reactionLoader.loadReaction(enabledChain.getValue());
				engine.addReaction(enabledChain.getKey(), reaction);
				loadedChains.put(enabledChain.getKey(), enabledChain.getValue());
			}

			/* wait before checking again. */
			Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
		}
	}

}
