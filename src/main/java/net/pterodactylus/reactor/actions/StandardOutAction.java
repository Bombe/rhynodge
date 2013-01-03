/*
 * Reactor - StandardOutAction.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.actions;

import net.pterodactylus.reactor.Action;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.output.Output;

/**
 * {@link Action} that simply dumps all {@link State}s to standard output.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StandardOutAction implements Action {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Output output) {
		System.out.println(String.format("Triggered by %s.", output.text("text/plain", -1)));
	}

}
