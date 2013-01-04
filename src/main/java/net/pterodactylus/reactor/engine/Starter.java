/*
 * Reactor - Starter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.engine;

import net.pterodactylus.reactor.loader.ChainWatcher;

/**
 * Reactor main starter class.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Starter {

	public static void main(String... arguments) {

		/* start the engine. */
		Engine engine = new Engine();
		engine.start();

		/* start a watcher. */
		ChainWatcher chainWatcher = new ChainWatcher(engine, "/home/bombe/Documents/Workspace/Reactor/src/main/resources/chains/");
		chainWatcher.start();
	}

}
