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

import java.io.IOException;

import net.pterodactylus.rhynodge.actions.EmailAction;
import net.pterodactylus.rhynodge.loader.ChainWatcher;
import net.pterodactylus.rhynodge.states.StateManager;
import net.pterodactylus.rhynodge.states.StateManager.Directory;
import net.pterodactylus.util.envopt.Parser;

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
	public static void main(String... arguments) throws IOException {

		Options options = Parser.fromSystemEnvironment().parseEnvironment(Options::new);

		/* create the state manager. */
		StateManager stateManager = new StateManager(Directory.of(options.stateDirectory));

		/* create the engine. */
		Engine engine = new Engine(stateManager, createErrorEmailAction(options.smtpHostname, options.errorEmailSender, options.errorEmailRecipient));

		/* start a watcher. */
		ChainWatcher chainWatcher = new ChainWatcher(engine, options.chainDirectory);
		chainWatcher.start();
	}

	private static EmailAction createErrorEmailAction(String smtpHostname, String errorEmailSender, String errorEmailRecipient) {
		return new EmailAction(smtpHostname, errorEmailSender, errorEmailRecipient);
	}

}
