/*
 * Reactor - AbstractState.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.states;

import net.pterodactylus.reactor.State;

/**
 * Abstract implementation of a {@link State} that knows about the basic
 * attributes of a {@link State}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractState implements State {

	/** The time of this state. */
	private final long time;

	/** Whether the state was successfully retrieved. */
	private final boolean success;

	/** The optional exception that occured while retrieving the state. */
	private final Throwable exception;

	/**
	 * Creates a new successful state.
	 */
	protected AbstractState() {
		this(true);
	}

	/**
	 * Creates a new state.
	 *
	 * @param success
	 *            {@code true} if the state is successful, {@code false}
	 *            otherwise
	 */
	protected AbstractState(boolean success) {
		this(success, null);
	}

	/**
	 * Creates a new non-successful state with the given exception.
	 *
	 * @param exception
	 *            The exception that occured while retrieving the state
	 */
	protected AbstractState(Throwable exception) {
		this(false, exception);
	}

	/**
	 * Creates a new state.
	 *
	 * @param success
	 *            {@code true} if the state is successful, {@code false}
	 *            otherwise
	 * @param exception
	 *            The exception that occured while retrieving the state
	 */
	protected AbstractState(boolean success, Throwable exception) {
		this.time = System.currentTimeMillis();
		this.success = success;
		this.exception = exception;
	}

	//
	// STATE METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long time() {
		return time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean success() {
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Throwable exception() {
		return exception;
	}

}
