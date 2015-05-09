package net.pterodactylus.util.inject;

import com.google.inject.Module;

/**
 * Helper class for Guice configuration.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ObjectBinding {

	public static <T> $1<T> forClass(Class<T> requestedClass) {
		return new $1<>(requestedClass);
	}

	public static class $1<T> {

		private final Class<T> requestedClass;

		private $1(Class<T> requestedClass) {
			this.requestedClass = requestedClass;
		}

		public Module is(T instance) {
			return (binder) -> binder.bind(requestedClass).toInstance(instance);
		}

	}

}
