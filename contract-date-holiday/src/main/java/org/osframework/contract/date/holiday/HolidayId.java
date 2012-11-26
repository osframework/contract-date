/**
 * File: HolidayId.java
 * 
 * Copyright 2012 David A. Joyce, OSFramework Project.
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
public class HolidayId implements Serializable {

	private static final long serialVersionUID = 4300354685200808447L;

	private int date;
	private String marketId = null;

	public HolidayId() {}

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
	 * @return ID of market to which the holiday belongs
	 */
	public String getMarketId() {
		return marketId;
	}

	/**
	 * Set Market ID of the identified <code>Holiday</code> instance.
	 * 
	 * @param marketId ID of market to which the holiday belongs
	 */
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + date;
		result = prime * result + ((marketId == null) ? 0 : marketId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		HolidayId other = (HolidayId) obj;
		return ((date == other.date) &&
				(null == marketId ? null == other.marketId : marketId.equals(other.marketId)));
	}

}
