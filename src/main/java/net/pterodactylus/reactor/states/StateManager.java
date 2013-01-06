/*
 * Reactor - StateManager.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.states;

import java.io.File;
import java.io.IOException;

import net.pterodactylus.reactor.State;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads and saves {@link State}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StateManager {

	/** The logger. */
	private static final Logger logger = Logger.getLogger(StateManager.class);

	/** Jackson object mapper. */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/** The directory in which to store states. */
	private final String directory;

	/**
	 * Creates a new state manager. The given directory is assumed to exist.
	 *
	 * @param directory
	 *            The directory to store states in
	 */
	public StateManager(String directory) {
		this.directory = directory;
	}

	//
	// ACTIONS
	//

	/**
	 * Loads the last state with the given name.
	 *
	 * @param reactionName
	 *            The name of the reaction
	 * @return The loaded state, or {@code null} if the state could not be
	 *         loaded
	 */
	public State loadLastState(String reactionName) {
		return loadLastState(reactionName, false);
	}

	/**
	 * Loads the last state with the given name.
	 *
	 * @param reactionName
	 *            The name of the reaction
	 * @return The loaded state, or {@code null} if the state could not be
	 *         loaded
	 */
	public State loadLastSuccessfulState(String reactionName) {
		return loadLastState(reactionName, true);
	}

	/**
	 * Saves the given state under the given name.
	 *
	 * @param reactionName
	 *            The name of the reaction
	 * @param state
	 *            The state to save
	 */
	public void saveState(String reactionName, State state) {
		try {
			File stateFile = stateFile(reactionName, "last");
			objectMapper.writeValue(stateFile, state);
			if (state.success()) {
				stateFile = stateFile(reactionName, "success");
				objectMapper.writeValue(stateFile, state);
			}
		} catch (JsonGenerationException jge1) {
			logger.warn(String.format("State for Reaction “%s” could not be generated.", reactionName), jge1);
		} catch (JsonMappingException jme1) {
			logger.warn(String.format("State for Reaction “%s” could not be generated.", reactionName), jme1);
		} catch (IOException ioe1) {
			logger.warn(String.format("State for Reaction “%s” could not be written.", reactionName));
		}
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Returns the file for the state with the given name.
	 *
	 * @param reactionName
	 *            The name of the reaction
	 * @param suffix
	 *            An additional suffix (may be {@code null}
	 * @return The file for the state
	 */
	private File stateFile(String reactionName, String suffix) {
		return new File(directory, reactionName + ((suffix != null) ? "." + suffix : "") + ".json");
	}

	/**
	 * Load the given state for the reaction with the given name.
	 *
	 * @param reactionName
	 *            The name of the reaction
	 * @param successful
	 *            {@code true} to load the last successful state, {@code false}
	 *            to load the last state
	 * @return The loaded state, or {@code null} if the state could not be
	 *         loaded
	 */
	private State loadLastState(String reactionName, boolean successful) {
		File stateFile = stateFile(reactionName, successful ? "success" : "last");
		try {
			State state = objectMapper.readValue(stateFile, AbstractState.class);
			return state;
		} catch (JsonParseException jpe1) {
			logger.warn(String.format("State for Reaction “%s” could not be parsed.", reactionName), jpe1);
		} catch (JsonMappingException jme1) {
			logger.warn(String.format("State for Reaction “%s” could not be parsed.", reactionName), jme1);
		} catch (IOException ioe1) {
			logger.info(String.format("State for Reaction “%s” could not be found.", reactionName));
		}
		return null;
	}

}
