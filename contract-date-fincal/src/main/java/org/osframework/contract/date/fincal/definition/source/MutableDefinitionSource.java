/*
 * File: MutableDefinitionSource.java
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
package org.osframework.contract.date.fincal.definition.source;

import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;

/**
 * Extended behavior of <tt>DefinitionSource</tt> objects that accept
 * changes.

 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface MutableDefinitionSource extends DefinitionSource {

	boolean addCentralBank(CentralBank centralBank);

	boolean removeCentralBank(CentralBank centralBank);

	boolean addHolidayDefinition(HolidayDefinition holidayDefinition);

	boolean removeHolidayDefinition(HolidayDefinition holidayDefinition);

	boolean addFinancialCalendar(FinancialCalendar financialCalendar);

	boolean removeFinancialCalendar(FinancialCalendar financialCalendar);

}
