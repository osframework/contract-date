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

import org.osframework.util.EqualsUtil;
import org.osframework.util.HashCodeUtil;

/**
 * Date of an observed <code>Holiday</code> on a financial calendar.
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
	public int hashCode() {
		if (0 == hashCode) {
			int result = super.hashCode();
			result = HashCodeUtil.hash(result, holidayDefinition);
			this.hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj) || getClass() != obj.getClass()) return false;
		final Holiday other = (Holiday) obj;
		return EqualsUtil.areEqual(holidayDefinition, other.holidayDefinition);
	}

}
