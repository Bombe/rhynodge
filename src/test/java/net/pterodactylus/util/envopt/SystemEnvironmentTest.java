package net.pterodactylus.util.envopt;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link SystemEnvironment}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class SystemEnvironmentTest {

	private final SystemEnvironment environment = new SystemEnvironment();

	@Test
	public void accessorCanAccessTheSystemEnvironment() {
		Map<String, String> systemEnvironment = System.getenv();
		MatcherAssert.assertThat(systemEnvironment.entrySet(), Matchers.not(Matchers.empty()));
		for (Entry<String, String> environmentEntry : systemEnvironment.entrySet()) {
			MatcherAssert.assertThat(environment.getValue(environmentEntry.getKey()), Matchers.is(
					Optional.of(environmentEntry.getValue())));
		}
	}

	@Test
	public void accessorRecognizesNonExistingVariables() {
		String randomName = generateRandomName();
		MatcherAssert.assertThat(environment.getValue(randomName), Matchers.is(Optional.empty()));
	}

	private String generateRandomName() {
		StringBuilder stringBuilder = new StringBuilder();
		do {
			stringBuilder.setLength(0);
			for (int i = 0; i < 10; i++) {
				stringBuilder.append((char) ('A' + (Math.random() * 26)));
			}
		} while (System.getenv(stringBuilder.toString()) != null);
		return stringBuilder.toString();
	}

}
