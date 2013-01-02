/*
 * Reactor - AlwaysTrigger.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.triggers;

import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.Trigger;

/**
 * {@link Trigger} implementation that always triggers.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class AlwaysTrigger implements Trigger {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean triggers(State currentState, State previousState) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object trigger() {
		return true;
	}

}