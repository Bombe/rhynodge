/*
 * Rhynodge - Starter.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.engine;

import net.pterodactylus.rhynodge.loader.ChainWatcher;
import net.pterodactylus.rhynodge.states.StateManager;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

/**
 * Rhynodge main starter class.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Starter {

	/**
	 * JVM main entry method.
	 *
	 * @param arguments
	 *            Command-line arguments
	 */
	public static void main(String... arguments) {

		/* parse command line. */
		Parameters parameters = CliFactory.parseArguments(Parameters.class, arguments);

		/* create the state manager. */
		StateManager stateManager = new StateManager(parameters.getStateDirectory());

		/* create the engine. */
		Engine engine = new Engine(stateManager);

		/* start a watcher. */
		ChainWatcher chainWatcher = new ChainWatcher(engine, parameters.getChainDirectory());
		chainWatcher.start();
	}

	/**
	 * Definition of the command-line parameters.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private static interface Parameters {

		/**
		 * Returns the directory to watch for chains.
		 *
		 * @return The chain directory
		 */
		@Option(defaultValue = "chains", shortName = "c", description = "The directory to watch for chains")
		String getChainDirectory();

		/**
		 * Returns the directory to store states in.
		 *
		 * @return The states directory
		 */
		@Option(defaultValue = "states", shortName = "s", description = "The directory to store states in")
		String getStateDirectory();

	}

}
