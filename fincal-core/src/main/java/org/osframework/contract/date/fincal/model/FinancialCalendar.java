/*
 * File: FinancialCalendar.java
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
import java.util.Collections;
import java.util.Currency;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Holiday calendar definition for a particular financial market. An instance of
 * this class is an iterable collection of <code>HolidayDefinition</code>s
 * representing the financial ("bank") holidays observed in the associated
 * market's country or economic zone.
 * <p>
 * The {@link #id} property of a <code>FinancialCalendar</code> object is not
 * arbitrary: valid values correspond to the financial calendar codes specified
 * by <b>financialcalendar.com</b>, the <i>de facto</i> authoritative source of
 * calendar data used by financial institutions worldwide.
 * </p>
 * <p>Instances of this class are immutable and thread-safe.</p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="http://www.financialcalendar.com/Data/Holidays/Coverage">
   Coverage - Financial Calendar</a>
 */
public class FinancialCalendar implements Iterable<HolidayDefinition>, Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -4226669744390855468L;

	private final String id;
	private final String description;
	private final CentralBank centralBank;
	private final Set<HolidayDefinition> holidayDefinitions;

	/**
	 * Cached hash value for this instance.
	 */
	private volatile transient int hashCode;

	/**
	 * Constructor. Holiday definitions passed to this constructor are copied to
	 * an new, unmodifiable collection which is safe for multi-thread concurrent
	 * iteration.
	 *
	 * @param id unique identifier for this instance
	 * @param description short description of this calendar definition
	 * @param centralBank central bank which governs this calendar's holiday definitions
	 * @param holidayDefinitions collection of holiday definitions which comprise this calendar
	 * @throws IllegalArgumentException if any argument is null or empty
	 */
	public FinancialCalendar(final String id,
			                 final String description,
			                 final CentralBank centralBank,
			                 final Set<HolidayDefinition> holidayDefinitions) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("Invalid blank 'id' argument");
		}
		if (StringUtils.isBlank(description)) {
			throw new IllegalArgumentException("Invalid blank 'description' argument");
		}
		if (null == centralBank) {
			throw new IllegalArgumentException("CentralBank argument cannot be null");
		}
		if (null == holidayDefinitions) {
			throw new IllegalArgumentException("Set argument cannot be null");
		}
		this.id = id;
		this.description = description;
		this.centralBank = centralBank;
		this.holidayDefinitions = Collections.unmodifiableSet(holidayDefinitions);
	}

	/**
	 * @return unique identifier for this instance
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return short description of this calendar definition
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return central bank which governs this calendar's holiday definitions
	 */
	public CentralBank getCentralBank() {
		return centralBank;
	}

	/**
	 * @return currency with which this calendar definition is associated
	 */
	public Currency getCurrency() {
		return centralBank.getCurrency();
	}

	/**
	 * Determine if this calendar contains the specified holiday definition.
	 * 
	 * @param hd holiday definition to be checked
	 * @return <code>true</code> if this calendar contains the holiday
	 *         definition, <code>false</code> otherwise
	 */
	public boolean contains(HolidayDefinition hd) {
		return holidayDefinitions.contains(hd);
	}

	/**
	 * @return unmodifiable iterator over the holiday definitions in this
	 *         calendar
	 */
	public Iterator<HolidayDefinition> iterator() {
		return holidayDefinitions.iterator();
	}

	/**
	 * @return number of holiday definitions in this calendar
	 */
	public int size() {
		return holidayDefinitions.size();
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
			               .append(description)
			               .append(centralBank)
			               .append(holidayDefinitions)
			               .toHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (this == obj) {
			result = true;
		} else if (obj instanceof FinancialCalendar) {
			final FinancialCalendar other = (FinancialCalendar)obj;
			result = new EqualsBuilder()
			             .append(id, other.id)
			             .append(description, other.description)
			             .append(centralBank, other.centralBank)
			             .append(holidayDefinitions, other.holidayDefinitions)
			             .isEquals();
		} else {
			result = false;
		}
		return result;
	}

}
