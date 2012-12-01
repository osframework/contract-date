/**
 * File: Holiday.java
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
 * Date observed / celebrated in a particular financial market on which no
 * business occurs (ie, the market is closed).
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class Holiday extends HolidayKey implements Serializable {

	private static final long serialVersionUID = -6646079945376287380L;

	private String holidayDefinitionId = null;

	public Holiday() {}

	/**
	 * @return the key
	 */
	public HolidayKey getKey() {
		return (HolidayKey)this;
	}

	/**
	 * @return the holidayDefinitionId
	 */
	public String getHolidayDefinitionId() {
		return holidayDefinitionId;
	}

	/**
	 * @param holidayDefinitionId the holidayDefinitionId to set
	 */
	public void setHolidayDefinitionId(String holidayDefinitionId) {
		this.holidayDefinitionId = holidayDefinitionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((holidayDefinitionId == null) ? 0 : holidayDefinitionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj) || getClass() != obj.getClass()) return false;
		Holiday other = (Holiday) obj;
		return (null == holidayDefinitionId ? null == other.holidayDefinitionId : holidayDefinitionId.equals(other.holidayDefinitionId));
	}

}
