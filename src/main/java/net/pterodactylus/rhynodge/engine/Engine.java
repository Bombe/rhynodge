/*
 * Rhynodge - Engine.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.states.AbstractState;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.StateManager;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * Rhynodge main engine.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Engine extends AbstractExecutionThreadService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(Engine.class);

	/** The state manager. */
	private final StateManager stateManager;

	/** All defined reactions. */
	/* synchronize on itself. */
	private final Map<String, Reaction> reactions = new HashMap<String, Reaction>();

	/**
	 * Creates a new engine.
	 *
	 * @param stateManager
	 *            The state manager
	 */
	public Engine(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	//
	// ACCESSORS
	//

	/**
	 * Adds the given reaction to this engine.
	 *
	 * @param name
	 *            The name of the reaction
	 * @param reaction
	 *            The reaction to add to this engine
	 */
	public void addReaction(String name, Reaction reaction) {
		synchronized (reactions) {
			reactions.put(name, reaction);
			reactions.notifyAll();
		}
	}

	/**
	 * Removes the reaction with the given name.
	 *
	 * @param name
	 *            The name of the reaction to remove
	 */
	public void removeReaction(String name) {
		synchronized (reactions) {
			if (!reactions.containsKey(name)) {
				return;
			}
			reactions.remove(name);
			reactions.notifyAll();
		}
	}

	//
	// ABSTRACTSERVICE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (isRunning()) {

			/* delay if we have no reactions. */
			synchronized (reactions) {
				if (reactions.isEmpty()) {
					logger.debug("Sleeping while no Reactions available.");
					try {
						reactions.wait();
					} catch (InterruptedException ie1) {
						/* ignore, we’re looping anyway. */
					}
					continue;
				}
			}

			/* find next reaction. */
			SortedMap<Long, Pair<String, Reaction>> nextReactions = Maps.newTreeMap();
			String reactionName;
			Reaction nextReaction;
			synchronized (reactions) {
				for (Entry<String, Reaction> reactionEntry : reactions.entrySet()) {
					net.pterodactylus.rhynodge.State state = stateManager.loadLastState(reactionEntry.getKey());
					long stateTime = (state != null) ? state.time() : 0;
					nextReactions.put(stateTime + reactionEntry.getValue().updateInterval(), Pair.of(reactionEntry.getKey(), reactionEntry.getValue()));
				}
				reactionName = nextReactions.get(nextReactions.firstKey()).getLeft();
				nextReaction = nextReactions.get(nextReactions.firstKey()).getRight();
			}
			logger.debug(String.format("Next Reaction: %s.", reactionName));

			/* wait until the next reaction has to run. */
			net.pterodactylus.rhynodge.State lastState = stateManager.loadLastState(reactionName);
			long lastStateTime = (lastState != null) ? lastState.time() : 0;
			int lastStateFailCount = (lastState != null) ? lastState.failCount() : 0;
			long waitTime = (lastStateTime + nextReaction.updateInterval()) - System.currentTimeMillis();
			logger.debug(String.format("Time to wait for next Reaction: %d millseconds.", waitTime));
			if (waitTime > 0) {
				synchronized (reactions) {
					try {
						logger.info(String.format("Waiting until %tc.", lastStateTime + nextReaction.updateInterval()));
						reactions.wait(waitTime);
					} catch (InterruptedException ie1) {
						/* we’re looping! */
					}
				}

				/* re-start loop to check for new reactions. */
				continue;
			}

			/* run reaction. */
			logger.info(String.format("Running Query for %s...", reactionName));
			Query query = nextReaction.query();
			net.pterodactylus.rhynodge.State state;
			try {
				logger.debug("Querying system...");
				state = query.state();
				if (state == null) {
					state = FailedState.INSTANCE;
				}
				logger.debug("System queried.");
			} catch (Throwable t1) {
				logger.warn("Querying system failed!", t1);
				state = new AbstractState(t1) {
					/* no further state. */
				};
			}
			logger.debug(String.format("State is %s.", state));

			/* convert states. */
			for (Filter filter : nextReaction.filters()) {
				if (state.success()) {
					net.pterodactylus.rhynodge.State newState = filter.filter(state);
					logger.debug(String.format("Old state is %s, new state is %s.", state, newState));
					state = newState;
				}
			}
			if (!state.success()) {
				state.setFailCount(lastStateFailCount + 1);
			}
			net.pterodactylus.rhynodge.State lastSuccessfulState = stateManager.loadLastSuccessfulState(reactionName);

			/* merge states. */
			boolean triggerHit = false;
			Trigger trigger = nextReaction.trigger();
			if ((lastSuccessfulState != null) && lastSuccessfulState.success() && state.success()) {
				net.pterodactylus.rhynodge.State newState = trigger.mergeStates(lastSuccessfulState, state);

				/* save new state. */
				stateManager.saveState(reactionName, newState);

				triggerHit = trigger.triggers();
			} else {
				/* save first or error state. */
				stateManager.saveState(reactionName, state);
			}

			/* run action if trigger was hit. */
			logger.debug(String.format("Trigger was hit: %s.", triggerHit));
			if (triggerHit) {
				logger.info("Executing Action...");
				nextReaction.action().execute(trigger.output(nextReaction));
			}

		}
	}

}
