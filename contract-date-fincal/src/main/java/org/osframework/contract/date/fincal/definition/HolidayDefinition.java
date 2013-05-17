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
package org.osframework.contract.date.fincal.definition;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osframework.contract.date.fincal.ImmutableEntity;

/**
 * Definition of a holiday observed in one or more financial markets.
 * <p>An instance of this class can provide an immutable and thread-safe
 * version of itself.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "holidayDefinition", propOrder = {
	"name",
	"description",
	"observance",
	"expression"
})
public class HolidayDefinition implements Serializable, HolidayExpression, ImmutableEntity<HolidayDefinition> {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -8997298685130920758L;

	private String id;
	private String name;
	private String description;
	private HolidayType observance;
	private String expression;

	/**
	 * Default constructor.
	 */
	public HolidayDefinition() {}

	/**
	 * Full constructor.
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
	 * @return unique identifier for this instance
	 */
	@XmlID
	@XmlAttribute(name = "id", required = true)
	public String getId() {
		return id;
	}

	/**
	 * Set unique identifier for this holiday definition.
	 * 
	 * @param id unique identifier for this instance
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return recognized name of the holiday defined by this object
	 */
	@XmlElement(name = "name", required = true)
	public String getName() {
		return name;
	}

	/**
	 * Set name of the holiday defined by this definition.
	 * 
	 * @param name recognized name of the holiday defined by this object
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description of the holiday defined by this object
	 */
	@XmlElement(name = "description", required = false)
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of this holiday defined by this definition.
	 * 
	 * @param description description of the holiday defined by this object
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return type of observance of holiday defined by this object
	 */
	@XmlElement(name = "observance", required = true)
	public HolidayType getObservance() {
		return observance;
	}

	/**
	 * Set holiday observance of this definition.
	 * 
	 * @param observance type of observance of holiday defined by this object
	 */
	public void setObservance(HolidayType observance) {
		this.observance = observance;
	}

	/**
	 * @return expression used to instances of the holiday defined by this object
	 */
	@XmlElement(name = "expression", required = true)
	public String getExpression() {
		return expression;
	}

	/**
	 * Set expression string for this definition.
	 * 
	 * @param expression expression used to instances of the holiday defined by this object
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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
	 * @throws IllegalStateException if observance or expression is null at time of invocation
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

	public HolidayDefinition toImmutable() {
		return new ImmutableHolidayDefinition(this.id, this.name, this.description, this.observance, this.expression);
	}

	@XmlTransient
	public boolean isImmutable() {
		return (this instanceof ImmutableHolidayDefinition);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
                   .append(id)
                   .append(name)
                   .append(description)
                   .append(observance)
                   .append(expression)
                   .toHashCode();
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

	/**
	 * Private immutable subclass of HolidayDefinition. This class is final; it
	 * is intended solely for instantiation by
	 * {@link HolidayDefinition#toImmutable()}.
	 *
	 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
	 */
	@XmlTransient
	private final class ImmutableHolidayDefinition extends HolidayDefinition {

		/**
		 * Serializable UID.
		 */
		private static final long serialVersionUID = 6143622333833253558L;
	
		/**
		 * Cached hash value for this instance.
		 */
		private volatile transient int hashCode;

		ImmutableHolidayDefinition(final String id,
                                   final String name,
                                   final String description,
                                   final HolidayType observance,
                                   final String expression) {
			super(id, name, description, observance, expression);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setId(String id) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setName(String name) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setDescription(String description) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setObservance(HolidayType observance) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setExpression(String expression) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * This implementation simply returns <code>this</code>.
		 */
		@Override
		public final HolidayDefinition toImmutable() {
			return this;
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
	}

}
