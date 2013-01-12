/*
 * File: HolidayKey.java
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

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.osframework.util.DateUtil;


/**
 * Unique composite identifier for a particular observed
 * <code>Holiday</code> on a financial calendar.
 * <p>Instances of this class are immutable and thread-safe.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayKey implements Comparable<HolidayKey>, Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -3548607801589534116L;

	private final FinancialCalendar financialCalendar;
	private final int date;

	/**
	 * Cached hash value for this instance.
	 */
	private volatile transient int hashCode;

	public HolidayKey(final FinancialCalendar financialCalendar, final int date) {
		if (null == financialCalendar) {
			throw new IllegalArgumentException("FinancialCalendar argument cannot be null");
		}
		this.financialCalendar = financialCalendar;
		this.date = date;
	}

	/**
	 * Constructor.
	 *
	 * @param financialCalendar financial calendar that owns the holiday identified by this key
	 * @param date date of holiday identified by this key
	 * @throws IllegalArgumentException if any argument is null or empty
	 */
	public HolidayKey(final FinancialCalendar financialCalendar, final Date date) {
		this(financialCalendar, DateUtil.formatDateToInt(date));
	}

	/**
	 * @return the financial calendar
	 */
	public FinancialCalendar getFinancialCalendar() {
		return financialCalendar;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	public int compareTo(HolidayKey o) {
		Validate.notNull(o, "HolidayKey to be compared cannot be null");
		int result = date - o.date;
		if (0 == result) {
			result = financialCalendar.getId().compareTo(o.financialCalendar.getId());
		}
		return result;
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = new HashCodeBuilder()
			               .append(date)
			               .append(financialCalendar)
			               .toHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (this == obj) {
			result = true;
		} else if (obj instanceof HolidayKey) {
			final HolidayKey other = (HolidayKey)obj;
			result = new EqualsBuilder()
			             .append(date, other.date)
			             .append(financialCalendar, other.financialCalendar)
			             .isEquals();
		} else {
			result = false;
		}
		return result;
	}

}
