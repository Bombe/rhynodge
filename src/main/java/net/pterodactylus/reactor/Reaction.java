/*
 * Reactor - Reaction.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor;

/**
 * A {@code Reaction} binds together {@link Query}s, {@link Trigger}s, and
 * {@link Action}s, and it stores the intermediary {@link State}s.
 *
 * @param <S>
 *            The type of the state
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Reaction<S extends State> {

	/** The query to run. */
	private final Query<S> query;

	/** The trigger to detect changes. */
	private final Trigger<S> trigger;

	/** The action to perform. */
	private final Action<S> action;

	/** The current state of the query. */
	private S currentState;

	/** The previous state of the query. */
	private S previousState;

	/**
	 * Creates a new reaction.
	 *
	 * @param query
	 *            The query to run
	 * @param trigger
	 *            The trigger to detect changes
	 * @param action
	 *            The action to perform
	 */
	public Reaction(Query<S> query, Trigger<S> trigger, Action<S> action) {
		this.query = query;
		this.trigger = trigger;
		this.action = action;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the query to run.
	 *
	 * @return The query to run
	 */
	public Query<S> query() {
		return query;
	}

	/**
	 * Returns the trigger to detect changes.
	 *
	 * @return The trigger to detect changes
	 */
	public Trigger<S> trigger() {
		return trigger;
	}

	/**
	 * Returns the action to perform.
	 *
	 * @return The action to perform
	 */
	public Action<S> action() {
		return action;
	}

	/**
	 * Returns the current state of the query.
	 *
	 * @return The current state of the query
	 */
	public S currentState() {
		return currentState;
	}

	/**
	 * Returns the previous state of the query.
	 *
	 * @return The previous state of the query
	 */
	public S previousState() {
		return previousState;
	}

}
