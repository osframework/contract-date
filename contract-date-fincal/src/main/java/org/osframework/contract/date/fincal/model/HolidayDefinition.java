/*
 * File: HolidayDefinition.java
 * 
 * Copyright 2012 OSFramework Project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osframework.contract.date.fincal.model;

import java.io.Serializable;

import org.osframework.contract.date.fincal.expression.HolidayExpression;

/**
 * Definition of a holiday observed in one or more financial markets.
 * <p>Instances of this class are immutable and thread-safe.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayDefinition implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 7746146194828405806L;

	private final String id;
	private final String name;
	private final String description;
	private final HolidayType observance;
	private final String expression;

	/**
	 * Constructor.
	 */
	public HolidayDefinition(final String id,
			                 final String name,
			                 final String description,
			                 final HolidayType observance,
			                 final String expression) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.observance = observance;
		this.expression = expression;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the observance
	 */
	public HolidayType getObservance() {
		return observance;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	public HolidayExpression createHolidayExpression() {
		if (null == observance || null == expression) {
			throw new IllegalStateException("Not in required state for invocation of method: createHolidayExpression");
		}
		return observance.toExpression(expression);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("HolidayDefinition[id='")
		                        .append(id).append("', name='")
		                        .append(name).append("'")
		                        .append((null == description) ? "" : (", description='" + description + "'"))
		                        .append("]");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((observance == null) ? 0 : observance.hashCode());
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		HolidayDefinition other = (HolidayDefinition) obj;
		return ((null == id ? null == other.id : id.equals(other.id)) &&
				(null == name ? null == other.name : name.equals(other.name)) &&
				(null == description ? null == other.description : description.equals(other.description)) &&
				(observance == other.observance) &&
				(null == expression ? null == other.expression : expression.equals(other.expression)));
	}

}
