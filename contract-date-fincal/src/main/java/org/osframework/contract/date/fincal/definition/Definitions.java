/*
 * File: Definitions.java
 * 
 * Copyright 2013 OSFramework Project.
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
package org.osframework.contract.date.fincal.definition;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Container for all financial calendar definition objects. Primarily used for
 * JAXB unmarshaling from XML to Java, prior to conversion to a
 * <tt>DefinitionSource</tt> object used across runtime operations.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see org.osframework.contract.date.fincal.definition.source.DefinitionSource
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "definitions")
@XmlType(name = "definitionsType", propOrder = {
	"centralBanks",
	"holidayDefinitions",
	"calendars"
})
public class Definitions {

	private Set<CentralBank> centralBanks = new HashSet<CentralBank>();
	private Set<HolidayDefinition> holidayDefinitions = new HashSet<HolidayDefinition>();
	private Set<FinancialCalendar> calendars = new HashSet<FinancialCalendar>();

	/**
	 * @return the centralBanks
	 */
	@XmlElementWrapper(name = "centralBanks", required = true)
	@XmlElements({
		@XmlElement(name = "centralBank", type = CentralBank.class)
	})
	public Set<CentralBank> getCentralBanks() {
		return centralBanks;
	}

	/**
	 * @param centralBanks the centralBanks to set
	 */
	public void setCentralBanks(Set<CentralBank> centralBanks) {
		this.centralBanks = centralBanks;
	}

	/**
	 * @return the holidayDefinitions
	 */
	@XmlElementWrapper(name = "holidayDefinitions", required = true)
	@XmlElements({
		@XmlElement(name = "holidayDefinition", type = HolidayDefinition.class)
	})
	public Set<HolidayDefinition> getHolidayDefinitions() {
		return holidayDefinitions;
	}

	/**
	 * @param holidayDefinitions the holidayDefinitions to set
	 */
	public void setHolidayDefinitions(Set<HolidayDefinition> holidayDefinitions) {
		this.holidayDefinitions = holidayDefinitions;
	}

	/**
	 * @return the calendars
	 */
	@XmlElementWrapper(name = "calendars", required = true)
	@XmlElements({
		@XmlElement(name = "calendar", type = FinancialCalendar.class)
	})
	public Set<FinancialCalendar> getCalendars() {
		return calendars;
	}

	/**
	 * @param calendars the calendars to set
	 */
	public void setCalendars(Set<FinancialCalendar> calendars) {
		this.calendars = calendars;
	}

}
