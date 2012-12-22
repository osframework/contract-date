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

import org.osframework.util.EqualsUtil;
import org.osframework.util.HashCodeUtil;

/**
 * Holiday calendar definition for a particular financial market. An instance of
 * this class is an iterable collection of <code>HolidayDefinition</code>s
 * representing the financial ("bank") holidays observed in the associated
 * market's country or economic zone.
 * <p>
 * The {@link #id} property of a <code>FinancialCalendar</code> object is not
 * arbitrary: valid values correspond to the financial calendar codes specified
 * by <b>financialcalendar.com</b>, the <i>de facto</i> authoritative source of
 * calendar data used by financial institutions worldwide.
 * </p>
 * <p>Instances of this class are immutable and thread-safe.</p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="http://www.financialcalendar.com/Data/Holidays/Coverage">financialcalendar.com: Coverage - Financial Calendar</a>
 */
public class FinancialCalendar implements Iterable<HolidayDefinition>, Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -4226669744390855468L;

	private final String id;
	private final String description;
	private final CentralBank centralBank;
	private final Set<HolidayDefinition> holidayDefinitions = new HashSet<HolidayDefinition>();

	private transient int hashCode;

	public FinancialCalendar(final String id,
			                 final String description,
			                 final CentralBank centralBank,
			                 final Set<HolidayDefinition> holidayDefinitions) {
		this.id = id;
		this.description = description;
		this.centralBank = centralBank;
		this.holidayDefinitions.addAll(holidayDefinitions);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the centralBank
	 */
	public CentralBank getCentralBank() {
		return centralBank;
	}

	public Currency getCurrency() {
		return (null == centralBank) ? null : centralBank.getCurrency();
	}

	public boolean contains(HolidayDefinition hd) {
		return this.holidayDefinitions.contains(hd);
	}

	public Iterator<HolidayDefinition> iterator() {
		return new UnmodifiableIterator();
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("FinancialCalendar[id='")
				.append(id).append("', description='").append(description)
				.append("', centralBank=").append(centralBank).append("]");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, this.id);
			result = HashCodeUtil.hash(result, this.description);
			result = HashCodeUtil.hash(result, this.centralBank);
			result = HashCodeUtil.hash(result, this.holidayDefinitions);
			this.hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof FinancialCalendar)) return false;
		final FinancialCalendar other = (FinancialCalendar)obj;
		return EqualsUtil.areEqual(id, other.id) &&
			   EqualsUtil.areEqual(description, other.description) &&
			   EqualsUtil.areEqual(centralBank, other.centralBank) &&
			   EqualsUtil.areEqual(holidayDefinitions, other.holidayDefinitions);
	}

	private class UnmodifiableIterator implements Iterator<HolidayDefinition> {

		private final transient Iterator<HolidayDefinition> it;

		private UnmodifiableIterator() {
			this.it = holidayDefinitions.iterator();
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public HolidayDefinition next() {
			return it.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
