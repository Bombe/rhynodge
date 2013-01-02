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

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import net.pterodactylus.reactor.Filter;
import net.pterodactylus.reactor.Query;
import net.pterodactylus.reactor.Reaction;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.states.AbstractState;
import net.pterodactylus.reactor.states.FailedState;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Uninterruptibles;

/**
 * Reactor main engine.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Engine extends AbstractExecutionThreadService {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(Engine.class);

	/** All defined reactions. */
	private final Set<Reaction> reactions = Sets.newHashSet();

	/** Reaction states. */
	private final Map<Reaction, ReactionExecution> reactionExecutions = Maps.newHashMap();

	//
	// ACCESSORS
	//

	/**
	 * Adds the given reaction to this engine.
	 *
	 * @param reaction
	 *            The reaction to add to this engine
	 */
	@SuppressWarnings("synthetic-access")
	public void addReaction(Reaction reaction) {
		reactions.add(reaction);
		reactionExecutions.put(reaction, new ReactionExecution());
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
			if (reactions.isEmpty()) {
				logger.trace("Sleeping for 1 second while no Reactions available.");
				Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
				continue;
			}

			/* find next reaction. */
			SortedMap<Long, Reaction> nextReactions = Maps.newTreeMap();
			for (Reaction reaction : reactions) {
				ReactionExecution reactionExecution = reactionExecutions.get(reaction);
				nextReactions.put(reactionExecution.lastExecutionTime() + reaction.updateInterval(), reaction);
			}
			Reaction nextReaction = nextReactions.get(nextReactions.firstKey());
			ReactionExecution reactionExecution = reactionExecutions.get(nextReaction);
			logger.debug(String.format("Next Reaction: %s.", nextReaction));

			/* wait until the next reaction has to run. */
			while (isRunning()) {
				long waitTime = (reactionExecution.lastExecutionTime() + nextReaction.updateInterval()) - System.currentTimeMillis();
				logger.debug(String.format("Time to wait for next Reaction: %d millseconds.", waitTime));
				if (waitTime <= 0) {
					break;
				}
				try {
					logger.debug(String.format("Waiting for %d milliseconds.", waitTime));
					TimeUnit.MILLISECONDS.sleep(waitTime);
				} catch (InterruptedException ie1) {
					/* we’re looping! */
				}
			}

			/* are we still running? */
			if (!isRunning()) {
				break;
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
				net.pterodactylus.reactor.State newState = filter.filter(state);
				logger.debug(String.format("Old state is %s, new state is %s.", state, newState));
				state = newState;
			}
			reactionExecution.addState(state);

			/* only run trigger if we have collected two states. */
			Trigger trigger = nextReaction.trigger();
			boolean triggerHit = false;
			if (reactionExecution.previousState() != null) {
				logger.debug("Checking Trigger for changes...");
				triggerHit = trigger.triggers(reactionExecution.currentState(), reactionExecution.previousState());
			}

			/* run action if trigger was hit. */
			logger.debug(String.format("Trigger was hit: %s.", triggerHit));
			if (triggerHit) {
				logger.info("Executing Action...");
				nextReaction.action().execute(trigger.trigger());
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
