/*
 * Rhynodge - AbstractWatcher.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.watchers;

import java.util.ArrayList;
import java.util.List;

import net.pterodactylus.rhynodge.Filter;
import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.Trigger;
import net.pterodactylus.rhynodge.Watcher;

/**
 * Abstract base implementation of a {@link Watcher}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class DefaultWatcher implements Watcher {

	/** The query of the watcher. */
	private final Query query;

	/** The filters of the watcher. */
	private final List<Filter> filters = new ArrayList<Filter>();

	/** The trigger of the watcher. */
	private final Trigger trigger;

	/**
	 * Creates a new default watcher.
	 *
	 * @param query
	 *            The query of the watcher
	 * @param filters
	 *            The filters of the watcher
	 * @param trigger
	 *            The trigger of the watcher
	 */
	protected DefaultWatcher(Query query, List<Filter> filters, Trigger trigger) {
		this.query = query;
		this.filters.addAll(filters);
		this.trigger = trigger;
	}

	//
	// WATCHER METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query query() {
		return query;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Filter> filters() {
		return filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trigger trigger() {
		return trigger;
	}

}
