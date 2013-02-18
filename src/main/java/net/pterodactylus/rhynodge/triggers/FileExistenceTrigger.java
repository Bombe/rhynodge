/*
 * Rhynodge - FileExistenceTrigger.java - Copyright © 2013 David Roden
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
import net.pterodactylus.rhynodge.states.FileState;

import com.google.common.base.Preconditions;

/**
 * A trigger that detects changes in the existence of a file.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class FileExistenceTrigger implements Trigger {

	/** Whether a change is triggered. */
	private boolean triggered;

	//
	// TRIGGER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State mergeStates(State previousState, State currentState) {
		Preconditions.checkState(previousState instanceof FileState, "previousState is not a FileState");
		Preconditions.checkState(currentState instanceof FileState, "currentState is not a FileState");
		triggered = ((FileState) previousState).exists() != ((FileState) currentState).exists();
		return currentState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers() {
		return triggered;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Output output(Reaction reaction) {
		return new DefaultOutput("File appeared/disappeared").addText("text/plain", "File appeared/disappeared").addText("text/html", "<div>File appeared/disappeared</div>");
	}

}
