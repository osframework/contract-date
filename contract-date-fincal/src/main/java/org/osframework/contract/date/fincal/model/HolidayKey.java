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

/**
 * Unique composite identifier for a particular observed
 * <code>Holiday</code> on a financial calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 *
 */
public class HolidayKey implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 9027617197436608599L;

	private FinancialCalendar financialCalendar = null;
	private int date;

	public HolidayKey() {}

	/**
	 * @return the financialCalendar
	 */
	public FinancialCalendar getFinancialCalendar() {
		return financialCalendar;
	}

	/**
	 * @param financialCalendar the financialCalendar to set
	 */
	public void setFinancialCalendar(FinancialCalendar financialCalendar) {
		this.financialCalendar = financialCalendar;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(int date) {
		this.date = date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.setDate(DateUtil.toInt(date));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + date;
		result = prime * result + ((financialCalendar == null) ? 0 : financialCalendar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof HolidayKey)) return false;
		HolidayKey other = (HolidayKey) obj;
		return ((date == other.date) &&
				(null == financialCalendar ? null == other.financialCalendar : financialCalendar.equals(other.financialCalendar)));
	}

}
