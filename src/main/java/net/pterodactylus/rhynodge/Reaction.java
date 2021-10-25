/*
 * Rhynodge - Reaction.java - Copyright © 2013 David Roden
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

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * A {@code Reaction} binds together {@link Query}s, {@link Merger}s, and
 * {@link Action}s, and it stores the intermediary {@link State}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Reaction {

	/** The name of this reaction. */
	private final String name;

	/** The query to run. */
	private final Query query;

	/** The filters to run. */
	private final List<Filter> filters = Lists.newArrayList();

	/** The merger merges old and new states. */
	private final Merger merger;

	/** The action to perform. */
	private final Action action;

	/** The interval in which to run queries (in milliseconds). */
	private long updateInterval;

	/**
	 * Creates a new reaction.
	 *
	 * @param name
	 *            The name of the reaction
	 * @param query
	 *            The query to run
	 * @param merger
	 *            The merger to merge states
	 * @param action
	 *            The action to perform
	 */
	public Reaction(String name, Query query, Merger merger, Action action) {
		this(name, query, Collections.<Filter> emptyList(), merger, action);
	}

	/**
	 * Creates a new reaction.
	 *
	 * @param name
	 *            The name of the reaction
	 * @param query
	 *            The query to run
	 * @param filters
	 *            The filters to run
	 * @param merger
	 *            The merger to merge states
	 * @param action
	 *            The action to perform
	 */
	public Reaction(String name, Query query, List<Filter> filters, Merger merger, Action action) {
		this.name = name;
		this.query = query;
		this.filters.addAll(filters);
		this.merger = merger;
		this.action = action;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the name of this reaction. This name is solely used for display
	 * purposes and does not need to be unique.
	 *
	 * @return The name of this reaction
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the query to run.
	 *
	 * @return The query to run
	 */
	public Query query() {
		return query;
	}

	/**
	 * Returns the filters to run.
	 *
	 * @return The filters to run
	 */
	public Iterable<Filter> filters() {
		return filters;
	}

	/**
	 * Returns the merger to merge states.
	 *
	 * @return The merger to merge states
	 */
	public Merger merger() {
		return merger;
	}

	/**
	 * Returns the action to perform.
	 *
	 * @return The action to perform
	 */
	public Action action() {
		return action;
	}

	/**
	 * Returns the update interval of this reaction.
	 *
	 * @return The update interval of this reaction (in milliseconds)
	 */
	public long updateInterval() {
		return updateInterval;
	}

	/**
	 * Sets the update interval of this reaction.
	 *
	 * @param updateInterval
	 *            The update interval of this reaction (in milliseconds)
	 * @return This reaction
	 */
	public Reaction setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
		return this;
	}

}
