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

import static com.google.common.collect.Maps.newTreeMap;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.states.AbstractState;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.StateManager;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

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
			Optional<NextReaction> nextReaction = getNextReaction();
			if (!nextReaction.isPresent()) {
				continue;
			}

			String reactionName = nextReaction.get().getKey();
			logger.debug(format("Next Reaction: %s.", reactionName));

			/* wait until the next reaction has to run. */
			long waitTime = nextReaction.get().getNextTime() - System.currentTimeMillis();
			logger.debug(format("Time to wait for next Reaction: %d millseconds.", waitTime));
			if (waitTime > 0) {
				waitForNextReactionToStart(nextReaction, waitTime);

				/* re-start loop to check for new reactions. */
				continue;
			}

			net.pterodactylus.rhynodge.State state = runReaction(nextReaction, reactionName);
			logger.debug(format("State is %s.", state));

			/* convert states. */
			for (Filter filter : nextReaction.get().getReaction().filters()) {
				if (state.success()) {
					state = filter.filter(state);
				}
			}
			if (!state.success()) {
				Optional<net.pterodactylus.rhynodge.State> lastState = stateManager.loadLastState(reactionName);
				state.setFailCount(lastState.map(net.pterodactylus.rhynodge.State::failCount).orElse(0) + 1);
			}
			Optional<net.pterodactylus.rhynodge.State> lastSuccessfulState = stateManager.loadLastSuccessfulState(reactionName);

			/* merge states. */
			if (lastSuccessfulState.isPresent() && lastSuccessfulState.get().success() && state.success()) {
				Trigger trigger = nextReaction.get().getReaction().trigger();
				net.pterodactylus.rhynodge.State newState = trigger.mergeStates(lastSuccessfulState.get(), state);
				stateManager.saveState(reactionName, newState);
				if (trigger.triggers()) {
					logger.info("Executing Action...");
					nextReaction.get().getReaction().action().execute(trigger.output(nextReaction.get().getReaction()));
				}
			} else {
				/* save first or error state. */
				stateManager.saveState(reactionName, state);
			}
		}
	}

	private net.pterodactylus.rhynodge.State runReaction(Optional<NextReaction> nextReaction, String reactionName) {
		logger.info(format("Running Query for %s...", reactionName));
		Query query = nextReaction.get().getReaction().query();
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
			};
		}
		return state;
	}

	private void waitForNextReactionToStart(Optional<NextReaction> nextReaction, long waitTime) {
		synchronized (reactions) {
			try {
				logger.info(format("Waiting until %tc.", nextReaction.get().getNextTime()));
				reactions.wait(waitTime);
			} catch (InterruptedException ie1) {
				/* we’re looping! */
			}
		}
	}

	private Optional<NextReaction> getNextReaction() {
		while (isRunning()) {
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
			SortedMap<Long, Pair<String, Reaction>> nextReactions = newTreeMap();
			synchronized (reactions) {
				for (Entry<String, Reaction> reactionEntry : reactions.entrySet()) {
					Optional<net.pterodactylus.rhynodge.State> state = stateManager.loadLastState(reactionEntry.getKey());
					long stateTime = state.isPresent() ? state.get().time() : 0;
					nextReactions.put(stateTime + reactionEntry.getValue().updateInterval(), Pair.of(reactionEntry.getKey(), reactionEntry.getValue()));
				}
				Pair<String, Reaction> keyReaction = nextReactions.get(nextReactions.firstKey());
				return of(new NextReaction(keyReaction.getKey(), keyReaction.getValue(), nextReactions.firstKey()));
			}
		}
		return empty();
	}

	private static class NextReaction {

		private final String key;
		private final Reaction reaction;
		private final long nextTime;

		private NextReaction(String key, Reaction reaction, long nextTime) {
			this.key = key;
			this.reaction = reaction;
			this.nextTime = nextTime;
		}

		public String getKey() {
			return key;
		}

		public Reaction getReaction() {
			return reaction;
		}

		public long getNextTime() {
			return nextTime;
		}

	}

}
