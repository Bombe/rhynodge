package net.pterodactylus.rhynodge.engine;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static net.pterodactylus.rhynodge.states.FailedState.INSTANCE;
import static org.apache.log4j.Logger.getLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import net.pterodactylus.rhynodge.Action;
import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.actions.EmailAction;
import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;
import net.pterodactylus.rhynodge.states.FailedState;

import org.apache.log4j.Logger;

/**
 * Runs a {@link Reaction}, starting with its {@link Query}, running the {@link
 * State} through its {@link Filter}s, and finally checking the {@link Trigger}
 * for whether an {@link Action} needs to be executed.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReactionRunner implements Runnable {

	private static final Logger logger = getLogger(ReactionRunner.class);
	private final Reaction reaction;
	private final ReactionState reactionState;
	private final EmailAction errorEmailAction;

	public ReactionRunner(Reaction reaction, ReactionState reactionState, EmailAction errorEmailAction) {
		this.reactionState = reactionState;
		this.reaction = reaction;
		this.errorEmailAction = errorEmailAction;
	}

	@Override
	public void run() {
		State state = runQuery();
		state = runStateThroughFilters(state);
		if (!state.success()) {
			logger.info(format("Reaction %s failed.", reaction.name()));
			saveStateWithIncreasedFailCount(state);
			errorEmailAction.execute(createErrorOutput(reaction, state));
			return;
		}
		Optional<State> lastSuccessfulState = reactionState.loadLastSuccessfulState();
		if (!lastSuccessfulState.isPresent()) {
			logger.info(format("No last state for %s.", reaction.name()));
			reactionState.saveState(state);
			return;
		}
		Trigger trigger = reaction.trigger();
		State newState = trigger.mergeStates(lastSuccessfulState.get(), state);
		reactionState.saveState(newState);
		if (trigger.triggers()) {
			logger.info(format("Trigger was hit for %s, executing action...", reaction.name()));
			reaction.action().execute(trigger.output(reaction));
		}
		logger.info(format("Reaction %s finished.", reaction.name()));
	}

	private void saveStateWithIncreasedFailCount(State state) {
		Optional<State> lastState = reactionState.loadLastState();
		state.setFailCount(lastState.map(State::failCount).orElse(0) + 1);
		reactionState.saveState(state);
	}

	private Output createErrorOutput(Reaction reaction, State state) {
		DefaultOutput output = new DefaultOutput(String.format("Error while processing “%s!”", reaction.name()));
		output.addText("text/plain", createErrorEmailText(reaction, state));
		output.addText("text/html", createErrorEmailText(reaction, state));
		return output;
	}

	private String createErrorEmailText(Reaction reaction, State state) {
		StringBuilder emailText = new StringBuilder();
		emailText.append(String.format("An error occured while processing “.”\n\n", reaction.name()));
		appendExceptionToEmailText(state.exception(), emailText);
		return emailText.toString();
	}

	private void appendExceptionToEmailText(Throwable exception, StringBuilder emailText) {
		if (exception != null) {
			try (StringWriter stringWriter = new StringWriter();
				 PrintWriter printWriter = new PrintWriter(stringWriter)) {
				exception.printStackTrace(printWriter);
				emailText.append(stringWriter.toString());
			} catch (IOException ioe1) {
				/* StringWriter doesn’t throw. */
				throw new RuntimeException(ioe1);
			}
		}
	}

	private State runQuery() {
		logger.info(format("Querying %s...", reaction.name()));
		try {
			return ofNullable(reaction.query().state()).orElse(INSTANCE);
		} catch (Throwable t1) {
			logger.warn(format("Could not query %s.", reaction.name()), t1);
			return new FailedState(t1);
		}
	}

	private State runStateThroughFilters(State state) {
		State currentState = state;
		for (Filter filter : reaction.filters()) {
			if (currentState.success()) {
				logger.debug(format("Filtering state through %s...", filter.getClass().getSimpleName()));
				try {
					currentState = filter.filter(currentState);
					if (currentState.success() && currentState.isEmpty()) {
						errorEmailAction.execute(createEmptyStateOutput(reaction, state));
					}
				} catch (Throwable t1) {
					logger.warn(format("Error during filter %s for %s.", filter.getClass().getSimpleName(), reaction.name()), t1);
					return new FailedState(t1);
				}
			}
		}
		return currentState;
	}

	private Output createEmptyStateOutput(Reaction reaction, State state) {
		DefaultOutput defaultOutput = new DefaultOutput(String.format("Reached Empty State for “%s!”", reaction.name()));
		defaultOutput.addText("text/plain", String.format("The %s for %s was empty.", state.getClass().getSimpleName(), reaction.name()));
		return defaultOutput;
	}

}
