package net.pterodactylus.rhynodge.engine;

import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link Configuration}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ConfigurationTest {

	@Test
	public void configurationCanBeReadFromJsonFile() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("configuration.json");
		Configuration configuration = Configuration.from(inputStream);
		MatcherAssert.assertThat(configuration.getSmtpHostname(), Matchers.is("localhost"));
		MatcherAssert.assertThat(configuration.getErrorEmailSender(), Matchers.is("errors@rhynodge.net"));
		MatcherAssert.assertThat(configuration.getErrorEmailRecipient(), Matchers.is("errors@user.net"));
	}

}
