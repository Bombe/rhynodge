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

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.actions.EmailAction;
import net.pterodactylus.rhynodge.states.StateManager;

/**
 * Rhynodge main engine.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
@Singleton
public class Engine {

	private final StateManager stateManager;
	private final ScheduledExecutorService executorService;
	private final Map<String, Future<?>> scheduledFutures = new ConcurrentHashMap<>();
	private final EmailAction errorEmailAction;

	@Inject
	public Engine(StateManager stateManager, EmailAction errorEmailAction) {
		this.stateManager = stateManager;
		this.errorEmailAction = errorEmailAction;
		executorService = new ScheduledThreadPoolExecutor(10);
	}

	//
	// ACCESSORS
	//

	/**
	 * Adds the given reaction to this engine.
	 *
	 * @param name
	 * 		The name of the reaction
	 * @param reaction
	 * 		The reaction to add to this engine
	 */
	public void addReaction(String name, Reaction reaction) {
		ReactionState reactionState = new ReactionState(stateManager, name);
		Optional<net.pterodactylus.rhynodge.State> lastState = reactionState.loadLastState();
		long lastExecutionTime = lastState.map(net.pterodactylus.rhynodge.State::time).orElse(0L);
		long nextExecutionTime = lastExecutionTime + reaction.updateInterval();
		ReactionRunner reactionRunner = new ReactionRunner(reaction, reactionState, errorEmailAction);
		ScheduledFuture<?> future = executorService.scheduleWithFixedDelay(reactionRunner, nextExecutionTime - currentTimeMillis(), reaction.updateInterval(), MILLISECONDS);
		scheduledFutures.put(name, future);
	}

	/**
	 * Removes the reaction with the given name.
	 *
	 * @param name
	 * 		The name of the reaction to remove
	 */
	public void removeReaction(String name) {
		if (!scheduledFutures.containsKey(name)) {
			return;
		}
		scheduledFutures.remove(name).cancel(true);
	}

}
