/*
 * File: Definitions.java
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
package org.osframework.contract.date.fincal.config;

import java.util.HashMap;
import java.util.Map;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Definitions description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class Definitions<S> {

	private Map<String, CentralBank> centralBanks = new HashMap<String, CentralBank>();
	private Map<String, HolidayDefinition> holidayDefinitions = new HashMap<String, HolidayDefinition>();
	private Map<String, FinancialCalendar> financialCalendars = new HashMap<String, FinancialCalendar>();

	protected S definitionSource;

	/**
	 * Constructor - default called from subclass constructors.
	 */
	protected Definitions() {}

	public boolean isEmpty() {
		return (centralBanks.isEmpty() &&
				holidayDefinitions.isEmpty() &&
				financialCalendars.isEmpty());
	}

	/**
	 * Constructor - creates deep copy clone of specified
	 * <code>Definitions</code> instance.
	 *
	 * @param originalDefinitions original definitions to be copied
	 */
	public Definitions(Definitions<?> originalDefinitions) {
		this.centralBanks.putAll(originalDefinitions.centralBanks);
		this.holidayDefinitions.putAll(originalDefinitions.holidayDefinitions);
		this.financialCalendars.putAll(originalDefinitions.financialCalendars);
	}

	public void addCentralBank(CentralBank centralBank) {
		if (!centralBanks.containsKey(centralBank.getId())) {
			centralBanks.put(centralBank.getId(), centralBank);
		}
	}

	public CentralBank getCentralBank(String id) {
		return centralBanks.get(id);
	}

	public void removeCentralBank(CentralBank centralBank) {
		if (centralBanks.containsKey(centralBank.getId())) {
			centralBanks.remove(centralBank.getId());
		}
	}

	public void addHolidayDefinition(HolidayDefinition holiday) {
		if (!holidayDefinitions.containsKey(holiday.getId())) {
			holidayDefinitions.put(holiday.getId(), holiday);
		}
	}

	public HolidayDefinition getHolidayDefinition(String id) {
		return holidayDefinitions.get(id);
	}

	public void removeHolidayDefinition(HolidayDefinition holiday) {
		if (holidayDefinitions.containsKey(holiday.getId())) {
			holidayDefinitions.remove(holiday.getId());
		}
	}

	public void addFinancialCalendar(FinancialCalendar calendar) {
		if (!financialCalendars.containsKey(calendar.getId())) {
			financialCalendars.put(calendar.getId(), calendar);
		}
	}

	public FinancialCalendar getFinancialCalendar(String id) {
		return financialCalendars.get(id);
	}

	public void removeFinancialCalendar(FinancialCalendar calendar) {
		if (financialCalendars.containsKey(calendar.getId())) {
			financialCalendars.remove(calendar.getId());
		}
	}

	public abstract void load() throws Exception;

}
