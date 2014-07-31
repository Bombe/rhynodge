/*
 * Rhynodge - AlwaysTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.triggers;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;

/**
 * {@link Trigger} implementation that always triggers.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AlwaysTrigger implements Trigger {

	private State currentState;

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns the current state.
	 */
	@Override
	public State mergeStates(State previousState, State currentState) {
		this.currentState = currentState;
		return currentState;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation always returns {@code true}.
	 */
	@Override
	public boolean triggers() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Output output(Reaction reaction) {
		return new DefaultOutput("true").addText("text/plain", "true").addText("text/html", "<div>true</div>");
	}

}
