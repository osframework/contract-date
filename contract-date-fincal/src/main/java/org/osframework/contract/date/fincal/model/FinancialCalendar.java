/*
 * File: FinancialCalendar.java
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
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Holiday calendar definition for a particular financial market. An instance of
 * this class is an iterable collection of <code>HolidayDefinition</code>s
 * representing the financial ("bank") holidays observed in the associated
 * market's country or economic zone.
 * <p>
 * The {@link #id} property of a <code>FinancialCalendar</code> object
 * is not arbitrary: valid values correspond to the financial calendar codes
 * specified by <b>financialcalendar.com</b>, the <i>de facto</i> authoritative
 * source of calendar data used by financial institutions worldwide.
 * </p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="http://www.financialcalendar.com/Data/Holidays/Coverage">financialcalendar.com: Coverage - Financial Calendar</a>
 */
public class FinancialCalendar implements Iterable<HolidayDefinition>, Serializable {

	private static final long serialVersionUID = 2814618163030685728L;

	private String id = null;
	private String description = null;
	private CentralBank centralBank = null;
	private Set<HolidayDefinition> holidayDefinitions = new HashSet<HolidayDefinition>();

	public FinancialCalendar() {}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the centralBank
	 */
	public CentralBank getCentralBank() {
		return centralBank;
	}

	/**
	 * @param centralBank the centralBank to set
	 */
	public void setCentralBank(CentralBank centralBank) {
		this.centralBank = centralBank;
	}

	public Currency getCurrency() {
		return (null == centralBank) ? null : centralBank.getCurrency();
	}

	public void add(HolidayDefinition hd) {
		this.holidayDefinitions.add(hd);
	}

	public void remove(HolidayDefinition hd) {
		this.holidayDefinitions.remove(hd);
	}

	public boolean contains(HolidayDefinition hd) {
		return this.holidayDefinitions.contains(hd);
	}

	public Iterator<HolidayDefinition> iterator() {
		return holidayDefinitions.iterator();
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("FinancialCalendar[id='").append(id)
				                .append("', description='").append(description)
				                .append("', centralBank=").append(centralBank)
				                .append("]");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((centralBank == null) ? 0 : centralBank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		FinancialCalendar other = (FinancialCalendar) obj;
		return ((null == id ? null == other.id : id.equals(other.id)) &&
				(null == description ? null == other.description : description.equals(other.description)) &&
				(null == centralBank ? null == other.centralBank : centralBank.equals(other.centralBank)));
	}

}
