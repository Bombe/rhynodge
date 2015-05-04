package net.pterodactylus.rhynodge.engine;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Stores general configuration of Rhynodge.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Configuration {

	@JsonProperty
	private final String smtpHostname = null;

	@JsonProperty
	private String errorEmailSender = null;

	@JsonProperty
	private String errorEmailRecipient = null;

	public String getSmtpHostname() {
		return smtpHostname;
	}

	public String getErrorEmailSender() {
		return errorEmailSender;
	}

	public String getErrorEmailRecipient() {
		return errorEmailRecipient;
	}

	public static Configuration from(InputStream inputStream) throws IOException {
		return new ObjectMapper().readValue(inputStream, Configuration.class);
	}

}
