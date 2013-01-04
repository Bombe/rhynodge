/*
 * Reactor - Engine.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import net.pterodactylus.reactor.Filter;
import net.pterodactylus.reactor.Query;
import net.pterodactylus.reactor.Reaction;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.AbstractState;
import net.pterodactylus.reactor.states.FailedState;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * Reactor main engine.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Engine extends AbstractExecutionThreadService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(Engine.class);

	/** All defined reactions. */
	/* synchronize on itself. */
	private final Map<String, Reaction> reactions = new HashMap<String, Reaction>();

	/** Reaction states. */
	/* synchronize on reactions. */
	private final Map<Reaction, ReactionExecution> reactionExecutions = Maps.newHashMap();

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
	 * @throws IllegalStateException
	 *             if the engine already contains a {@link Reaction} with the
	 *             given name
	 */
	@SuppressWarnings("synthetic-access")
	public void addReaction(String name, Reaction reaction) {
		synchronized (reactions) {
			if (reactions.containsKey(name)) {
				throw new IllegalStateException(String.format("Engine already contains a Reaction named “%s!”", name));
			}
			reactions.put(name, reaction);
			reactionExecutions.put(reaction, new ReactionExecution());
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
			Reaction reaction = reactions.remove(name);
			reactionExecutions.remove(reaction);
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
			SortedMap<Long, Reaction> nextReactions = Maps.newTreeMap();
			Reaction nextReaction;
			ReactionExecution reactionExecution;
			synchronized (reactions) {
				for (Reaction reaction : reactions.values()) {
					nextReactions.put(reactionExecutions.get(reaction).lastExecutionTime() + reaction.updateInterval(), reaction);
				}
				nextReaction = nextReactions.get(nextReactions.firstKey());
				reactionExecution = reactionExecutions.get(nextReaction);
			}
			logger.debug(String.format("Next Reaction: %s.", nextReaction));

			/* wait until the next reaction has to run. */
			long waitTime = (reactionExecution.lastExecutionTime() + nextReaction.updateInterval()) - System.currentTimeMillis();
			logger.debug(String.format("Time to wait for next Reaction: %d millseconds.", waitTime));
			if (waitTime > 0) {
				synchronized (reactions) {
					try {
						logger.debug(String.format("Waiting for %d milliseconds.", waitTime));
						reactions.wait(waitTime);
					} catch (InterruptedException ie1) {
						/* we’re looping! */
					}
				}

				/* re-start loop to check for new reactions. */
				continue;
			}

			/* run reaction. */
			reactionExecution.setLastExecutionTime(System.currentTimeMillis());
			Query query = nextReaction.query();
			net.pterodactylus.reactor.State state;
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
					net.pterodactylus.reactor.State newState = filter.filter(state);
					logger.debug(String.format("Old state is %s, new state is %s.", state, newState));
					state = newState;
				}
			}
			if (state.success()) {
				reactionExecution.addState(state);
			}

			/* only run trigger if we have collected two states. */
			Trigger trigger = nextReaction.trigger();
			boolean triggerHit = false;
			if ((reactionExecution.previousState() != null) && state.success()) {
				logger.debug("Checking Trigger for changes...");
				triggerHit = trigger.triggers(reactionExecution.currentState(), reactionExecution.previousState());
			}

			/* run action if trigger was hit. */
			logger.debug(String.format("Trigger was hit: %s.", triggerHit));
			if (triggerHit) {
				logger.info("Executing Action...");
				nextReaction.action().execute(trigger.output());
			}

		}
	}

	/**
	 * Stores execution states of a {@link Reaction}.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static class ReactionExecution {

		/** The time the reaction was last executed. */
		private long lastExecutionTime;

		/** The previous state of the reaction. */
		private net.pterodactylus.reactor.State previousState;

		/** The current state of the reaction. */
		private net.pterodactylus.reactor.State currentState;

		//
		// ACCESSORS
		//

		/**
		 * Returns the time the reaction was last executed. If the reaction was
		 * not yet executed, this method returns {@code 0}.
		 *
		 * @return The last execution time of the reaction (in milliseconds
		 *         since Jan 1, 1970 UTC)
		 */
		public long lastExecutionTime() {
			return lastExecutionTime;
		}

		/**
		 * Returns the current state of the reaction. If the reaction was not
		 * yet executed, this method returns {@code null}.
		 *
		 * @return The current state of the reaction
		 */
		public net.pterodactylus.reactor.State currentState() {
			return currentState;
		}

		/**
		 * Returns the previous state of the reaction. If the reaction was not
		 * yet executed at least twice, this method returns {@code null}.
		 *
		 * @return The previous state of the reaction
		 */
		public net.pterodactylus.reactor.State previousState() {
			return previousState;
		}

		/**
		 * Sets the last execution time of the reaction.
		 *
		 * @param lastExecutionTime
		 *            The last execution time of the reaction (in milliseconds
		 *            since Jan 1, 1970 UTC)
		 * @return This execution
		 */
		public ReactionExecution setLastExecutionTime(long lastExecutionTime) {
			this.lastExecutionTime = lastExecutionTime;
			return this;
		}

		//
		// ACTIONS
		//

		/**
		 * Adds the given state as current state and moves the current state
		 * into the previous state.
		 *
		 * @param state
		 *            The new current state
		 * @return This execution
		 */
		public ReactionExecution addState(net.pterodactylus.reactor.State state) {
			previousState = currentState;
			currentState = state;
			return this;
		}

	}

}
