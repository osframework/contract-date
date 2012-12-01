/**
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
package org.osframework.contract.date.holiday;

import java.io.Serializable;

/**
 * Unique composite identifier for a particular <code>Holiday</code> instance.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class HolidayKey implements Serializable {

	private static final long serialVersionUID = 2433761068755065660L;

	private int date;
	private String financialCalendarDefinitionId = null;

	public HolidayKey() {}

	/**
	 * @return compact integer notation of holiday date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * Set date of the identified <code>Holiday</code> instance.
	 * 
	 * @param date compact integer notation of holiday date
	 */
	public void setDate(int date) {
		this.date = date;
	}

	/**
	 * @return ID of financial calendar to which the holiday belongs
	 */
	public String getFinancialCalendarDefinitionId() {
		return financialCalendarDefinitionId;
	}

	/**
	 * Set financial calendar ID of the identified <code>Holiday</code>
	 * instance.
	 * 
	 * @param financialCalendarDefinitionId ID of financial calendar to which
	 *        the holiday belongs
	 */
	public void setFinancialCalendarDefinitionId(String financialCalendarDefinitionId) {
		this.financialCalendarDefinitionId = financialCalendarDefinitionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + date;
		result = prime * result + ((financialCalendarDefinitionId == null) ? 0 : financialCalendarDefinitionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof HolidayKey)) return false;
		HolidayKey other = (HolidayKey) obj;
		return ((date == other.date) &&
				(null == financialCalendarDefinitionId ? null == other.financialCalendarDefinitionId : financialCalendarDefinitionId.equals(other.financialCalendarDefinitionId)));
	}

}
