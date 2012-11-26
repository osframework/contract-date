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
public class Holiday implements Serializable {

	private static final long serialVersionUID = -6888162151807801798L;

	private HolidayId id = new HolidayId();
	private HolidayDefinition definition = null;
	private HolidayType type = null;

	public Holiday() {}

	/**
	 * @return the id
	 */
	public HolidayId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(HolidayId id) {
		this.id = id;
	}

	/**
	 * @return
	 * @see org.osframework.contract.date.holiday.HolidayId#getDate()
	 */
	public int getDate() {
		return id.getDate();
	}

	/**
	 * @param date
	 * @see org.osframework.contract.date.holiday.HolidayId#setDate(int)
	 */
	public void setDate(int date) {
		id.setDate(date);
	}

	/**
	 * @return the definition
	 */
	public HolidayDefinition getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(HolidayDefinition definition) {
		this.definition = definition;
	}

	/**
	 * @return
	 * @see org.osframework.contract.date.holiday.HolidayId#getMarketId()
	 */
	public String getMarketId() {
		return id.getMarketId();
	}

	/**
	 * @param marketId
	 * @see org.osframework.contract.date.holiday.HolidayId#setMarketId(java.lang.String)
	 */
	public void setMarketId(String marketId) {
		id.setMarketId(marketId);
	}

	/**
	 * @return the type
	 */
	public HolidayType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(HolidayType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(id.getDate())
		                        .append(" (").append(id.getMarketId()).append(") ")
		                        .append((null != definition) ? definition.getName() : "UNKNOWN");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Holiday other = (Holiday) obj;
		return ((null == id ? null == other.id : id.equals(other.id)) &&
				(type == other.type) &&
				(null == definition ? null == other.definition : definition.equals(other.definition)));
	}

}
