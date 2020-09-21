/*
 * Rhynodge - State.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge;

import javax.annotation.Nonnull;

import net.pterodactylus.rhynodge.output.Output;

/**
 * Defines the current state of a system.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface State {

	/**
	 * Returns the time when this state was retrieved.
	 *
	 * @return The time when this state was retrieved (in millseconds since Jan
	 *         1, 1970 UTC)
	 */
	long time();

	/**
	 * Whether the state was successfully retrieved. This method should only
	 * return {@code true} if a meaningful result could be retrieved; if e. g. a
	 * service is currently not reachable, this method should return false
	 * instead of emulating success by using empty lists or similar constructs.
	 *
	 * @return {@code true} if the state could be retrieved successfully,
	 *         {@code false} otherwise
	 */
	boolean success();

	boolean isEmpty();

	/**
	 * Returns the number of consecutive failures. This method only returns a
	 * meaningful number iff {@link #success()} returns {@code false}. If
	 * {@link #success()} returns {@code false} for the first time after
	 * returning {@code true} and this method is called after {@link #success()}
	 * it will return {@code 1}.
	 *
	 * @return The number of consecutive failures
	 */
	int failCount();

	/**
	 * Sets the fail count of this state.
	 *
	 * @param failCount
	 *            The fail count of this state
	 */
	void setFailCount(int failCount);

	/**
	 * If {@link #success()} returns {@code false}, this method may return a
	 * {@link Throwable} to give some details for the reason why retrieving the
	 * state was not possible. For example, network-based {@link Query}s might
	 * return any exception that were encountered while communicating with the
	 * remote service.
	 *
	 * @return An exception that occured, may be {@code null} in case an
	 *         exception can not be meaningfully returned
	 */
	Throwable exception();

	@Nonnull
	Output output(Reaction reaction);

}
