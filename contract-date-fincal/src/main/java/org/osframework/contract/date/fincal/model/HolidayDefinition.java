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
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osframework.contract.date.fincal.expression.HolidayExpression;

/**
 * Definition of a holiday observed in one or more financial markets.
 * <p>Instances of this class are immutable and thread-safe.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayDefinition implements HolidayExpression, Serializable {

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
	 * Cached hash value for this instance.
	 */
	private volatile transient int hashCode;

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

	/**
	 * Produce the date of a defined holiday for the specified year. This
	 * implementation delegates to the object returned by the
	 * {@link #toHolidayExpression()} method.
	 * 
	 * @throws IllegalStateException if this definition is missing required
	 *                               state
	 */
	public Date evaluate(int year) {
		return toHolidayExpression().evaluate(year);
	}

	/**
	 * Returns a <code>HolidayExpression</code> representation of the object.
	 * 
	 * @return  a <code>HolidayExpression</code> representation of the object
	 */
	public HolidayExpression toHolidayExpression() {
		if (null == observance || null == expression) {
			throw new IllegalStateException("Not in required state for invocation of method: createHolidayExpression");
		}
		return observance.toExpression(expression);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = new HashCodeBuilder()
			               .append(id)
			               .append(name)
			               .append(description)
			               .append(observance)
			               .append(expression)
			               .toHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals;
		if (this == obj) {
			equals = true;
		} else if (obj instanceof HolidayDefinition) {
			final HolidayDefinition other = (HolidayDefinition)obj;
			equals = new EqualsBuilder()
			             .append(id, other.id)
			             .append(name, other.name)
			             .append(description, other.description)
			             .append(observance, other.observance)
			             .append(expression, other.expression)
			             .isEquals();
		} else {
			equals = false;
		}
		return equals;
	}

}
