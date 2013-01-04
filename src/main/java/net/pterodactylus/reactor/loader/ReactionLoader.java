/*
 * Reactor - Loader.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.pterodactylus.reactor.Action;
import net.pterodactylus.reactor.Filter;
import net.pterodactylus.reactor.Query;
import net.pterodactylus.reactor.Reaction;
import net.pterodactylus.reactor.Trigger;
import net.pterodactylus.reactor.loader.Chain.Parameter;
import net.pterodactylus.reactor.loader.Chain.Part;

/**
 * Creates {@link Reaction}s from {@link Chain}s.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReactionLoader {

	/**
	 * Creates a {@link Reaction} from the given {@link Chain}.
	 *
	 * @param chain
	 *            The chain to create a reaction from
	 * @return The created reaction
	 * @throws LoaderException
	 *             if a class can not be loaded
	 */
	@SuppressWarnings("static-method")
	public Reaction loadReaction(Chain chain) throws LoaderException {

		/* check if chain is enabled. */
		if (!chain.enabled()) {
			throw new IllegalArgumentException("Chain is not enabled.");
		}

		/* create query. */
		Query query = createObject(chain.query().name(), "net.pterodactylus.reactor.queries", extractParameters(chain.query().parameters()));

		/* create filters. */
		List<Filter> filters = new ArrayList<Filter>();
		for (Part filterPart : chain.filters()) {
			filters.add(ReactionLoader.<Filter> createObject(filterPart.name(), "net.pterodactylus.reactor.filters", extractParameters(filterPart.parameters())));
		}

		/* create trigger. */
		Trigger trigger = createObject(chain.trigger().name(), "net.pterodactylus.reactor.triggers", extractParameters(chain.trigger().parameters()));

		/* create action. */
		Action action = createObject(chain.action().name(), "net.pterodactylus.reactor.actions", extractParameters(chain.action().parameters()));

		return new Reaction(query, filters, trigger, action).setUpdateInterval(TimeUnit.SECONDS.toMillis(chain.updateInterval()));
	}

	//
	// STATIC METHODS
	//

	/**
	 * Extracts all parameter values from the given parameters.
	 *
	 * @param parameters
	 *            The parameters to extract the values from
	 * @return The extracted values
	 */
	private static List<String> extractParameters(List<Parameter> parameters) {
		List<String> parameterValues = new ArrayList<String>();

		for (Parameter parameter : parameters) {
			parameterValues.add(parameter.value());
		}

		return parameterValues;
	}

	/**
	 * Creates a new object.
	 * <p>
	 * First, {@code className} is used to try to load a {@link Class} with that
	 * name. If that fails, {@code packageName} is prepended to the class name.
	 * If no class can be found, a {@link LoaderException} will be thrown.
	 * <p>
	 * If a class could be located using the described method, a constructor
	 * will be searched that has the same number of {@link String} parameters as
	 * the given parameters. The parameters from the given parameters are then
	 * used in a constructor call to create the new object.
	 *
	 * @param className
	 *            The name of the class
	 * @param packageName
	 *            The optional name of the package to prepend
	 * @param parameters
	 *            The parameters for the constructor call
	 * @return The created object
	 * @throws LoaderException
	 *             if the object can not be created
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createObject(String className, String packageName, List<String> parameters) throws LoaderException {

		/* try to load class without package name. */
		Class<?> objectClass = null;
		try {
			objectClass = Class.forName(className);
		} catch (ClassNotFoundException cnfe1) {
			/* ignore, we’ll try again. */
		}

		if (objectClass == null) {
			try {
				objectClass = Class.forName(packageName + "." + className);
			} catch (ClassNotFoundException cnfe1) {
				/* okay, now we need to throw. */
				throw new LoaderException(String.format("Could find neither class “%s” nor class “%s.”", className, packageName + "." + className), cnfe1);
			}
		}

		/* locate an eligible constructor. */
		Constructor<?> wantedConstructor = null;
		for (Constructor<?> constructor : objectClass.getConstructors()) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length != parameters.size()) {
				continue;
			}
			boolean compatibleTypes = true;
			for (Class<?> parameterType : parameterTypes) {
				if (parameterType != String.class) {
					compatibleTypes = false;
					break;
				}
			}
			if (!compatibleTypes) {
				continue;
			}
			wantedConstructor = constructor;
		}

		if (wantedConstructor == null) {
			throw new LoaderException("Could not find eligible constructor.");
		}

		try {
			return (T) wantedConstructor.newInstance(parameters.toArray());
		} catch (IllegalArgumentException iae1) {
			throw new LoaderException("Could not invoke constructor.", iae1);
		} catch (InstantiationException ie1) {
			throw new LoaderException("Could not invoke constructor.", ie1);
		} catch (IllegalAccessException iae1) {
			throw new LoaderException("Could not invoke constructor.", iae1);
		} catch (InvocationTargetException ite1) {
			throw new LoaderException("Could not invoke constructor.", ite1);
		}

	}

}
