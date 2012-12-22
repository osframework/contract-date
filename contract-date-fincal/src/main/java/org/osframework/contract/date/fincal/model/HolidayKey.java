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

import org.osframework.contract.date.util.DateUtil;
import org.osframework.util.EqualsUtil;
import org.osframework.util.HashCodeUtil;

/**
 * Unique composite identifier for a particular observed
 * <code>Holiday</code> on a financial calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayKey implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -3548607801589534116L;

	private final FinancialCalendar financialCalendar;
	private final int date;

	private transient int hashCode;

	public HolidayKey(final FinancialCalendar financialCalendar, final int date) {
		this.financialCalendar = financialCalendar;
		this.date = date;
	}

	public HolidayKey(final FinancialCalendar financialCalendar, final Date date) {
		this(financialCalendar, DateUtil.toInt(date));
	}

	/**
	 * @return the financialCalendar
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

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, date);
			result = HashCodeUtil.hash(result, financialCalendar);
			this.hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof HolidayKey)) return false;
		final HolidayKey other = (HolidayKey) obj;
		return EqualsUtil.areEqual(date, other.date) &&
			   EqualsUtil.areEqual(financialCalendar, other.financialCalendar);
	}

}
