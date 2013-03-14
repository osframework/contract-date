/*
 * File: DefinitionSource.java
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

import java.util.Iterator;


/**
 * Core required behavior of objects that act as a central source of definitions
 * used to generate financial calendar holidays.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface DefinitionSource {

	CentralBank getCentralBank(String centralBankId);

	Iterator<CentralBank> centralBankIterator();

	HolidayDefinition getHolidayDefinition(String holidayDefinitionId);

	Iterator<HolidayDefinition> holidayDefinitionIterator();

	FinancialCalendar getFinancialCalendar(String financialCalendarId);

	Iterator<FinancialCalendar> financialCalendarIterator();

}
