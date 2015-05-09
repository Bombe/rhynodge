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
import java.util.Arrays;

import net.pterodactylus.rhynodge.actions.EmailAction;
import net.pterodactylus.rhynodge.loader.ChainWatcher;
import net.pterodactylus.rhynodge.loader.ChainWatcher.ChainDirectory;
import net.pterodactylus.rhynodge.states.StateManager.StateDirectory;
import net.pterodactylus.util.envopt.Parser;
import net.pterodactylus.util.inject.ObjectBinding;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
		EmailAction errorEmailAction =
				createErrorEmailAction(options.smtpHostname, options.errorEmailSender, options.errorEmailRecipient);

		Injector injector = Guice.createInjector(Arrays.asList(
				ObjectBinding.forClass(StateDirectory.class).is(StateDirectory.of(options.stateDirectory)),
				ObjectBinding.forClass(ChainDirectory.class).is(ChainDirectory.of(options.chainDirectory)),
				ObjectBinding.forClass(EmailAction.class).is(errorEmailAction)
		));

		/* start a watcher. */
		ChainWatcher chainWatcher = injector.getInstance(ChainWatcher.class);
		chainWatcher.start();
	}

	private static EmailAction createErrorEmailAction(String smtpHostname, String errorEmailSender, String errorEmailRecipient) {
		return new EmailAction(smtpHostname, errorEmailSender, errorEmailRecipient);
	}

}
