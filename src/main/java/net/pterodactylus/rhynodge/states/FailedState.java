/*
 * Rhynodge - FailedState.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.states;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.annotation.Nonnull;

import net.pterodactylus.rhynodge.State;

/**
 * {@link State} implementation that signals failure.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FailedState extends AbstractState {

	/** A failed state instance without an exception. */
	public static final State INSTANCE = new FailedState();

	/**
	 * Creates a new failed state.
	 */
	public FailedState() {
		super(false);
	}

	/**
	 * Creates a new failed state with the given exception
	 *
	 * @param exception
	 *            The exception of the state
	 */
	public FailedState(Throwable exception) {
		super(exception);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Nonnull
	@Override
	protected String plainText() {
		if (exception() == null) {
			return "Failed";
		}
		try (Writer stringWriter = new StringWriter();
			 PrintWriter printWriter = new PrintWriter(stringWriter)) {
			exception().printStackTrace(printWriter);
			return "Failed: " + stringWriter.toString();
		} catch (IOException ioe1) {
			return "Failed while rendering exception";
		}
	}

	//
	// STATIC METHODS
	//

	/**
	 * Returns a failed state for the given state. The failed state will be
	 * unsuccessful ({@link #success()} returns false) and it will contain the
	 * same {@link #exception()} as the given state.
	 *
	 * @param state
	 *            The state to copy the exception from
	 * @return A failed state
	 */
	public static FailedState from(State state) {
		if (state instanceof FailedState) {
			return (FailedState) state;
		}
		return new FailedState(state.exception());
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s[exception=%s]", getClass().getSimpleName(), exception());
	}

}
