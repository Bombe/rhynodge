/*
 * Rhynodge - Chain.java - Copyright © 2013 David Roden
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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for chain definitions.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Chain {

	/**
	 * Parameter model.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Parameter {

		/** The name of the parameter. */
		@JsonProperty
		private String name;

		/** The value of the parameter. */
		@JsonProperty
		private String value;

		/**
		 * Returns the name of the parameter.
		 *
		 * @return The name of the parameter
		 */
		public String name() {
			return name;
		}

		/**
		 * Returns the value of the parameter.
		 *
		 * @return The value of the parameter
		 */
		public String value() {
			return value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			int hashCode = 0;
			hashCode ^= name.hashCode();
			hashCode ^= value.hashCode();
			return hashCode;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Parameter)) {
				return false;
			}
			Parameter parameter = (Parameter) object;
			if (!name.equals(parameter.name)) {
				return false;
			}
			return value.equals(parameter.value);
		}

	}

	/**
	 * Defines a part of a chain.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	public static class Part {

		/** The class name of the part. */
		@JsonProperty(value = "class")
		private String name;

		/** The parameters of the part. */
		@JsonProperty
		private List<Parameter> parameters = new ArrayList<Parameter>();

		/**
		 * Returns the name of the part’s class.
		 *
		 * @return The name of the part’s class
		 */
		public String name() {
			return name;
		}

		/**
		 * Returns the parameters of the part.
		 *
		 * @return The parameters of the part
		 */
		public List<Parameter> parameters() {
			return parameters;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			int hashCode = 0;
			hashCode ^= name.hashCode();
			for (Parameter parameter : parameters) {
				hashCode ^= parameter.hashCode();
			}
			return hashCode;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Part)) {
				return false;
			}
			Part part = (Part) object;
			if (!name.equals(part.name)) {
				return false;
			}
			if (parameters.size() != part.parameters.size()) {
				return false;
			}
			for (int parameterIndex = 0; parameterIndex < parameters.size(); ++parameterIndex) {
				if (!parameters.get(parameterIndex).equals(part.parameters.get(parameterIndex))) {
					return false;
				}
			}
			return true;
		}

	}

	/** Whether this chain is enabled. */
	@JsonProperty
	private boolean enabled;

	/** The name of the chain. */
	@JsonProperty
	private String name;

	/** The query of the chain. */
	@JsonProperty
	private Part query;

	/** The filters of the chain. */
	@JsonProperty
	private List<Part> filters = new ArrayList<Part>();

	/** The trigger of the chain. */
	@JsonProperty
	private Part trigger;

	/** A combination of query, filters, and a trigger. */
	@JsonProperty
	private Part watcher;

	/** The action of the chain. */
	@JsonProperty
	private Part action;

	/** Interval between updates (in seconds). */
	@JsonProperty
	private int updateInterval;

	/**
	 * Returns whether this chain is enabled.
	 *
	 * @return {@code true} if this chain is enabled, {@code false} otherwise
	 */
	public boolean enabled() {
		return enabled;
	}

	/**
	 * Returns the name of the chain.
	 *
	 * @return The name of the chain
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the query of this chain.
	 *
	 * @return The query of this chain
	 */
	public Part query() {
		return query;
	}

	/**
	 * Returns the filters of this chain.
	 *
	 * @return The filters of this chain
	 */
	public List<Part> filters() {
		return filters;
	}

	/**
	 * Returns the trigger of this chain.
	 *
	 * @return The trigger of this chain
	 */
	public Part trigger() {
		return trigger;
	}

	/**
	 * Returns an optional watcher.
	 *
	 * @return The watcher of this chain
	 */
	public Part watcher() {
		return watcher;
	}

	/**
	 * Returns the action of this chain.
	 *
	 * @return The action of this chain
	 */
	public Part action() {
		return action;
	}

	/**
	 * Returns the update interval of the chain.
	 *
	 * @return The update interval (in seconds)
	 */
	public int updateInterval() {
		return updateInterval;
	}

	//
	// OBJECT METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode ^= name.hashCode();
		if (watcher != null) {
			hashCode ^= watcher.hashCode();
		} else {
			hashCode ^= query.hashCode();
			for (Part filter : filters) {
				hashCode ^= filter.hashCode();
			}
			hashCode ^= trigger.hashCode();
		}
		hashCode ^= action.hashCode();
		hashCode ^= updateInterval;
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Chain)) {
			return false;
		}
		Chain chain = (Chain) object;
		if (!name.equals(chain.name)) {
			return false;
		}
		if (watcher != null) {
			if (!watcher.equals(chain.watcher)) {
				return false;
			}
		} else {
			if (!query.equals(chain.query)) {
				return false;
			}
			if (filters.size() != chain.filters.size()) {
				return false;
			}
			for (int filterIndex = 0; filterIndex < filters.size(); ++filterIndex) {
				if (!filters.get(filterIndex).equals(chain.filters.get(filterIndex))) {
					return false;
				}
			}
			if (!trigger.equals(chain.trigger)) {
				return false;
			}
		}
		if (!action.equals(chain.action)) {
			return false;
		}
		return updateInterval == chain.updateInterval;
	}

}
