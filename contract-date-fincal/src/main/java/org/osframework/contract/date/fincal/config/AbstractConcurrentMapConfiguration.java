/**
 * File: AbstractConcurrentMapConfiguration.java
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
package org.osframework.contract.date.fincal.config;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Abstract superclass of objects that provide financial calendar definitions
 * in a concurrent, multi-threaded context. Provides internal
 * <code>ConcurrentMap</code> caches and <code>Iterator</code> implementations
 * to support thread safety.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractConcurrentMapConfiguration extends AbstractConfiguration {

	protected final ConcurrentMap<String, CentralBank> centralBanks;
	protected final ConcurrentMap<String, HolidayDefinition> holidayDefinitions;
	protected final ConcurrentMap<String, FinancialCalendar> financialCalendars;

	/**
	 * Default constructor. Initializes <code>ConcurrentMap</code> objects for
	 * in-memory caching of loaded definitions. 
	 */
	public AbstractConcurrentMapConfiguration() {
		super();
		this.centralBanks = new ConcurrentHashMap<String, CentralBank>();
		this.holidayDefinitions = new ConcurrentHashMap<String, HolidayDefinition>();
		this.financialCalendars = new ConcurrentHashMap<String, FinancialCalendar>();
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#addCentralBank(org.osframework.contract.date.fincal.model.CentralBank)
	 */
	@Override
	public boolean addCentralBank(CentralBank centralBank) {
		return (null == centralBanks.putIfAbsent(centralBank.getId(), centralBank));
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#removeCentralBank(org.osframework.contract.date.fincal.model.CentralBank)
	 */
	@Override
	public boolean removeCentralBank(CentralBank centralBank) {
		return centralBanks.remove(centralBank.getId(), centralBank);
	}

	/**
	 * Get the cached <code>CentralBank</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public CentralBank getCentralBank(String centralBankId) {
		return centralBanks.get(centralBankId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>CentralBank</code> objects.
	 */
	public Iterator<CentralBank> centralBankIterator() {
		return new CentralBankIterator();
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#addHolidayDefinition(org.osframework.contract.date.fincal.model.HolidayDefinition)
	 */
	@Override
	public boolean addHolidayDefinition(HolidayDefinition holidayDefinition) {
		return (null == holidayDefinitions.putIfAbsent(holidayDefinition.getId(), holidayDefinition));
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#removeHolidayDefinition(org.osframework.contract.date.fincal.model.HolidayDefinition)
	 */
	@Override
	public boolean removeHolidayDefinition(HolidayDefinition holidayDefinition) {
		return holidayDefinitions.remove(holidayDefinition.getId(), holidayDefinition);
	}

	/**
	 * Get the cached <code>HolidayDefinition</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public HolidayDefinition getHolidayDefinition(String holidayDefinitionId) {
		return holidayDefinitions.get(holidayDefinitionId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>HolidayDefinition</code> objects.
	 */
	public Iterator<HolidayDefinition> holidayDefinitionIterator() {
		return new HolidayDefinitionIterator();
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#addFinancialCalendar(org.osframework.contract.date.fincal.model.FinancialCalendar)
	 */
	@Override
	public boolean addFinancialCalendar(FinancialCalendar financialCalendar) {
		return (null == financialCalendars.putIfAbsent(financialCalendar.getId(), financialCalendar));
	}

	/* (non-Javadoc)
	 * @see org.osframework.contract.date.fincal.config.Configuration#removeFinancialCalendar(org.osframework.contract.date.fincal.model.FinancialCalendar)
	 */
	@Override
	public boolean removeFinancialCalendar(FinancialCalendar financialCalendar) {
		return financialCalendars.remove(financialCalendar.getId(), financialCalendar);
	}

	/**
	 * Get the cached <code>FinancialCalendar</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public FinancialCalendar getFinancialCalendar(String financialCalendarId) {
		return financialCalendars.get(financialCalendarId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>FinancialCalendar</code> objects.
	 */
	public Iterator<FinancialCalendar> financialCalendarIterator() {
		return new FinancialCalendarIterator();
	}

	protected final class CentralBankIterator implements Iterator<CentralBank> {
	
		final Iterator<String> centralBankIdIt;
	
		protected CentralBankIterator() {
			this.centralBankIdIt = centralBanks.keySet().iterator();
		}
	
		public boolean hasNext() {
			return centralBankIdIt.hasNext();
		}
	
		public CentralBank next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return centralBanks.get(centralBankIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	protected final class HolidayDefinitionIterator implements Iterator<HolidayDefinition> {
	
		final Iterator<String> holidayDefIdIt;
	
		protected HolidayDefinitionIterator() {
			this.holidayDefIdIt = holidayDefinitions.keySet().iterator();
		}
	
		public boolean hasNext() {
			return holidayDefIdIt.hasNext();
		}
	
		public HolidayDefinition next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return holidayDefinitions.get(holidayDefIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	protected final class FinancialCalendarIterator implements Iterator<FinancialCalendar> {
	
		final Iterator<String> finCalIdIt;
	
		protected FinancialCalendarIterator() {
			this.finCalIdIt = financialCalendars.keySet().iterator();
		}
	
		public boolean hasNext() {
			return finCalIdIt.hasNext();
		}
	
		public FinancialCalendar next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return financialCalendars.get(finCalIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
