package net.pterodactylus.rhynodge.engine;

import java.util.Optional;

import net.pterodactylus.rhynodge.Reaction;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.StateManager;

/**
 * Allows simple access to a {@link Reaction}’s saved states without exposing
 * the key used to access the state on disk.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReactionState {

	private final StateManager stateManager;
	private final String reactionName;

	public ReactionState(StateManager stateManager, String reactionName) {
		this.stateManager = stateManager;
		this.reactionName = reactionName;
	}

	public Optional<State> loadLastState() {
		return stateManager.loadLastState(reactionName);
	}

	public Optional<State> loadLastSuccessfulState() {
		return stateManager.loadLastSuccessfulState(reactionName);
	}

	public void saveState(State state) {
		stateManager.saveState(reactionName, state);
	}

}
