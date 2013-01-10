/*
 * Rhynodge - LoaderException.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.loader;

/**
 * Exception that signals a problem when loading chain XML files.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class LoaderException extends Exception {

	/**
	 * Creates a new loader exception.
	 */
	public LoaderException() {
		super();
	}

	/**
	 * Creates a new loader exception.
	 *
	 * @param message
	 *            The message of the exception
	 */
	public LoaderException(String message) {
		super(message);
	}

	/**
	 * Creates a new loader exception.
	 *
	 * @param throwable
	 *            The root cause
	 */
	public LoaderException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Creates a new loader exception.
	 *
	 * @param message
	 *            The message of the exception
	 * @param throwable
	 *            The root cause
	 */
	public LoaderException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
