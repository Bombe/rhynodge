package net.pterodactylus.rhynodge.engine;

import net.pterodactylus.util.envopt.Option;

/**
 * Options for Rhynodge which must be set as environment variables.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Options {

	@Option(name = "SMTP_HOSTNAME", defaultValue = "localhost")
	public final String smtpHostname = null;

	@Option(name = "ERROR_EMAIL_SENDER", required = true)
	public final String errorEmailSender = null;

	@Option(name = "ERROR_EMAIL_RECIPIENT", required = true)
	public final String errorEmailRecipient = null;

	@Option(name = "STATE_DIRECTORY", defaultValue = "states")
	public final String stateDirectory = null;

	@Option(name = "CHAIN_DIRECTORY", defaultValue = "chains")
	public final String chainDirectory = null;

}
