/*
 * File: Holiday.java
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

/**
 * Date of an observed holiday on a financial calendar.
 * <p>Instances of this class are immutable and thread-safe.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Holiday extends HolidayKey implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 8157412688065685626L;

	private final HolidayDefinition holidayDefinition;

	/**
	 * Cached hash value for this instance.
	 */
	private transient int hashCode;

	/**
	 * Constructor - description here.
	 */
	public Holiday(final FinancialCalendar financialCalendar,
			       final int date,
			       final HolidayDefinition holidayDefinition) {
		super(financialCalendar, date);
		this.holidayDefinition = holidayDefinition;
	}

	public Holiday(final FinancialCalendar financialCalendar,
			       final Date date,
			       final HolidayDefinition holidayDefinition) {
		super(financialCalendar, date);
		this.holidayDefinition = holidayDefinition;
	}

	/**
	 * @return the holidayDefinition
	 */
	public HolidayDefinition getHolidayDefinition() {
		return holidayDefinition;
	}

	public HolidayKey getKey() {
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = new HashCodeBuilder()
			               .appendSuper(super.hashCode())
			               .append(holidayDefinition)
			               .toHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (this == obj) {
			result = true;
		} else if (obj instanceof Holiday) {
			final Holiday other = (Holiday)obj;
			result = new EqualsBuilder()
			             .appendSuper(super.equals(other))
			             .append(holidayDefinition, other.holidayDefinition)
			             .isEquals();
		} else {
			result = false;
		}
		return result;
	}

}
