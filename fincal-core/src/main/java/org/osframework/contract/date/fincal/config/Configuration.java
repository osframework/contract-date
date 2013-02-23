/*
 * File: Configuration.java
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

import java.util.Iterator;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Core required behavior of objects that act as a central source of definitions
 * used to generate financial calendar holidays.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface Configuration {

	boolean addCentralBank(CentralBank centralBank);

	boolean removeCentralBank(CentralBank centralBank);

	CentralBank getCentralBank(String centralBankId);

	Iterator<CentralBank> centralBankIterator();

	boolean addHolidayDefinition(HolidayDefinition holidayDefinition);

	boolean removeHolidayDefinition(HolidayDefinition holidayDefinition);

	HolidayDefinition getHolidayDefinition(String holidayDefinitionId);

	Iterator<HolidayDefinition> holidayDefinitionIterator();

	boolean addFinancialCalendar(FinancialCalendar financialCalendar);

	boolean removeFinancialCalendar(FinancialCalendar financialCalendar);

	FinancialCalendar getFinancialCalendar(String financialCalendarId);

	Iterator<FinancialCalendar> financialCalendarIterator();

}