/*
 * File: ObjectMother.java
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
package org.osframework.contract.date.fincal;

import java.net.URL;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.osframework.contract.date.fincal.model.HolidayType;

/**
 * Provides complex dependency construction in support of test cases.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class ObjectMother {

	public static final String HOLIDAY_DEF_ID_MLK_DAY = "MLKDay";
	public static final String HOLIDAY_DEF_ID_INDEPENDENCE_DAY = "IndependenceDay";
	public static final String HOLIDAY_DEF_ID_THANKSGIVING = "ThanksgivingUS";
	public static final String[] HOLIDAY_DEF_IDS = { HOLIDAY_DEF_ID_MLK_DAY,
		                                             HOLIDAY_DEF_ID_INDEPENDENCE_DAY,
		                                             HOLIDAY_DEF_ID_THANKSGIVING };

	/**
	 * Private constructor.
	 */
	private ObjectMother() {}

	public static CentralBank createCentralBank() {
		return new CentralBank("USFR", "United States Federal Reserve", "US", Currency.getInstance("USD"));
	}

	public static HolidayDefinition createHolidayDefinition(String defId) {
		HolidayDefinition hd = null;
		if (HOLIDAY_DEF_ID_MLK_DAY.equals(defId)) {
			hd = new HolidayDefinition("MLKDay",
                                       "Martin Luther King Day",
                                       "Birthday of Martin Luther King, Jr.",
                                       HolidayType.RELATIVE,
                                       "JANUARY/MONDAY/3");
		} else if (HOLIDAY_DEF_ID_INDEPENDENCE_DAY.equals(defId)) {
			hd = new HolidayDefinition("IndependenceDay",
                                       "Independence Day",
                                       "Celebration of 1776 Declaration of Independence",
                                       HolidayType.FIXED,
                                       "JULY/01");
		} else if (HOLIDAY_DEF_ID_THANKSGIVING.equals(defId)) {
			hd = new HolidayDefinition("ThanksgivingUS",
                                       "Thanksgiving",
                                       "Thanksgiving (US)",
                                       HolidayType.RELATIVE,
                                       "NOVEMBER/THURSDAY/4");
		} else {
			throw new IllegalArgumentException("Unknown holiday definition ID: " + defId);
		}
		return hd;
	}

	public static Set<HolidayDefinition> createHolidayDefinitions() {
		Set<HolidayDefinition> hds = new HashSet<HolidayDefinition>();
		for (String defId : HOLIDAY_DEF_IDS) {
			hds.add(createHolidayDefinition(defId));
		}
		return hds;
	}

	public static FinancialCalendar createFinancialCalendar() {
		return new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), createHolidayDefinitions());
	}

	public static URL loadDefinitionsXML() {
		return ObjectMother.class.getResource("/definitions-test.xml");
	}

}
