package net.pterodactylus.rhynodge.states;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.pterodactylus.rhynodge.State;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link State} implementation that can expose itself as plain text and/or
 * HTML.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class OutputState extends AbstractState {

	private final Optional<String> plainTextOutput;
	private final Optional<String> htmlOutput;

	public OutputState() {
		this(Optional.<String>empty(), Optional.<String>empty());
	}

	public OutputState(Optional<String> plainTextOutput, Optional<String> htmlOutput) {
		this.plainTextOutput = plainTextOutput;
		this.htmlOutput = htmlOutput;
	}

	@Override
	public boolean isEmpty() {
		return !plainTextOutput.isPresent() && !htmlOutput.isPresent();
	}

	@Nonnull
	@Override
	protected String plainText() {
		return plainTextOutput.orElse("");
	}

	@Nullable
	@Override
	protected String htmlText() {
		return htmlOutput.orElse(null);
	}

}
