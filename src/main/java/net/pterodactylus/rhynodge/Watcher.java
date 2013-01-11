/*
 * Rhynodge - Watcher.java - Copyright © 2013 David Roden
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

import java.util.List;

/**
 * A {@code Watcher} combines a {@link Query}, a number of {@link Filter}s, and
 * a {@link Trigger}, as these parts are closely related after all.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public interface Watcher {

	/**
	 * Returns the query of the watcher.
	 *
	 * @return The query of the watcher
	 */
	public Query query();

	/**
	 * Returns the filters of the watcher.
	 *
	 * @return The filters of the watcher
	 */
	public List<Filter> filters();

	/**
	 * Returns the trigger of the watcher.
	 *
	 * @return The trigger of the watcher
	 */
	public Trigger trigger();

}
