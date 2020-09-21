/*
 * Rhynodge - AbstractState.java - Copyright © 2013 David Roden
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

/**
 * Abstract implementation of a {@link State} that knows about the basic
 * attributes of a {@link State}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class AbstractState implements State {

	/** The time of this state. */
	@JsonProperty
	private final long time;

	/** Whether the state was successfully retrieved. */
	private final boolean success;
	private final boolean empty;

	/** The optional exception that occured while retrieving the state. */
	private final Throwable exception;

	/** The number of consecutive failures. */
	@JsonProperty
	private int failCount;

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
		this(success, true, null);
	}

	protected AbstractState(boolean success, boolean empty) {
		this(success, empty, null);
	}

	/**
	 * Creates a new non-successful state with the given exception.
	 *
	 * @param exception
	 *            The exception that occured while retrieving the state
	 */
	protected AbstractState(Throwable exception) {
		this(false, true, exception);
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
	protected AbstractState(boolean success, boolean empty, Throwable exception) {
		this.time = System.currentTimeMillis();
		this.success = success;
		this.empty = empty;
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

	@Override
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int failCount() {
		return failCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Throwable exception() {
		return exception;
	}

	@Nonnull
	@Override
	public Output output(Reaction reaction) {
		return new DefaultOutput(summary(reaction))
				.addText("text/plain", plainText())
				.addText("text/html", htmlText());
	}

	@Nonnull
	protected String summary(Reaction reaction) {
		return reaction.name();
	}

	@Nonnull
	protected abstract String plainText();

	@Nullable
	protected String htmlText() {
		//noinspection UnstableApiUsage
		return "<div>" + htmlEscaper.escape(plainText()) + "</div>";
	}

	@SuppressWarnings("UnstableApiUsage")
	private static final Escaper htmlEscaper = HtmlEscapers.htmlEscaper();

}
