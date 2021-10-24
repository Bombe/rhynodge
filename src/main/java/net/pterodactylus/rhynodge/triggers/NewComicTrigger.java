/*
 * rhynodge - NewComicTrigger.java - Copyright © 2013 David Roden
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.states.ComicState;
import net.pterodactylus.rhynodge.states.ComicState.Comic;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * {@link Trigger} implementation that detects the presence of new {@link
 * Comic}s in a {@link ComicState}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class NewComicTrigger implements Trigger {

	private boolean triggered = false;

	@Override
	public State mergeStates(State previousState, State currentState) {
		checkArgument(previousState instanceof ComicState, "previous state must be a comic state");
		checkArgument(currentState instanceof ComicState, "current state must be a comic state");

		ComicState previousComicState = (ComicState) previousState;
		ComicState currentComicState = (ComicState) currentState;

		List<Comic> allComics = new ArrayList<>(previousComicState.comics());
		Set<Comic> newComics = new HashSet<>();

		for (Comic comic : currentComicState) {
			if (!allComics.contains(comic)) {
				allComics.add(comic);
				newComics.add(comic);
				triggered = true;
			}
		}

		return new ComicState(allComics, newComics);
	}

	@Override
	public boolean triggers() {
		return triggered;
	}

}
