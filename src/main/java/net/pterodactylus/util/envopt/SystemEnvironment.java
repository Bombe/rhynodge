package net.pterodactylus.util.envopt;

import java.util.Optional;

/**
 * {@link Environment} implementation that reads variables from the system environment.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 * @see System#getenv(String)
 */
public class SystemEnvironment implements Environment {

	@Override
	public Optional<String> getValue(String name) {
		return Optional.ofNullable(System.getenv(name));
	}

}
