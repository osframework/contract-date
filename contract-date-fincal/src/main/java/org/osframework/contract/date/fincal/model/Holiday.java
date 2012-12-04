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

/**
 * Date of an observed <code>Holiday</code> on a financial calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 *
 */
public class Holiday extends HolidayKey implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -4708527844688969229L;

	private HolidayDefinition holidayDefinition = null;

	/**
	 * Constructor - description here.
	 */
	public Holiday() {
		super();
	}

	/**
	 * @return the holidayDefinition
	 */
	public HolidayDefinition getHolidayDefinition() {
		return holidayDefinition;
	}

	/**
	 * @param holidayDefinition the holidayDefinition to set
	 */
	public void setHolidayDefinition(HolidayDefinition holidayDefinition) {
		this.holidayDefinition = holidayDefinition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((holidayDefinition == null) ? 0 : holidayDefinition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj) || getClass() != obj.getClass()) return false;
		Holiday other = (Holiday) obj;
		return (null == holidayDefinition ? null == other.holidayDefinition : holidayDefinition.equals(other.holidayDefinition));
	}

}
