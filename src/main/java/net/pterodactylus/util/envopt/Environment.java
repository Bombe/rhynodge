package net.pterodactylus.util.envopt;

import java.util.Optional;

/**
 * An environment is a read-only key-value store, with keys and values both being {@link String}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Environment {

	Optional<String> getValue(String name);

}
