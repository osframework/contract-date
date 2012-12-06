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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Definitions description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class Definitions {

	protected static final String SCOPE_PREFIX_GLOBAL = "";

	private Map<String, CentralBank> centralBanks = new HashMap<String, CentralBank>();
	private Map<String, HolidayDefinition> holidayDefinitions = new HashMap<String, HolidayDefinition>();
	private Map<String, FinancialCalendar> financialCalendars = new HashMap<String, FinancialCalendar>();

	public void addCentralBank(CentralBank centralBank) {
		centralBanks.put(centralBank.getId(), centralBank);
	}

	public boolean containsCentralBank(CentralBank centralBank) {
		return centralBanks.containsKey(centralBank.getId());
	}

	public Iterator<CentralBank> centralBankIterator() {
		return Collections.unmodifiableMap(centralBanks).values().iterator();
	}

	public void addHolidayDefinition(HolidayDefinition holidayDefinition) {
		this.addHolidayDefinition(holidayDefinition, null);
	}

	public void addHolidayDefinition(HolidayDefinition holidayDefinition, FinancialCalendar financialCalendar) {
		String key = buildKey((null == financialCalendar ? null : financialCalendar.getId()), holidayDefinition.getId());
		holidayDefinitions.put(key, holidayDefinition);
	}

	public boolean containsHolidayDefinition(HolidayDefinition holidayDefinition) {
		Set<String> keys = holidayDefinitions.keySet();
		boolean found = false;
		for (String key : keys) {
			if (key.endsWith(holidayDefinition.getId())) {
				found = true;
				break;
			}
		}
		return found;
	}

	public Iterator<HolidayDefinition> holidayDefinitionIterator() {
		return Collections.unmodifiableMap(holidayDefinitions).values().iterator();
	}

	public void addFinancialCalendar(FinancialCalendar financialCalendar) {
		financialCalendars.put(financialCalendar.getId(), financialCalendar);
	}

	public boolean containsFinancialCalendar(FinancialCalendar financialCalendar) {
		return financialCalendars.containsKey(financialCalendar.getId());
	}

	public Iterator<FinancialCalendar> financialCalendarIterator() {
		return Collections.unmodifiableMap(financialCalendars).values().iterator();
	}

	protected String buildKey(String prefix, String objectId) {
		return (SCOPE_PREFIX_GLOBAL.equals(prefix)) ? objectId : (SCOPE_PREFIX_GLOBAL + "/" + objectId);
	}

	protected abstract void load() throws IOException;

}
