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

import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.definition.HolidayType;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.osframework.contract.date.fincal.holiday.HolidayId;
import org.osframework.contract.date.fincal.output.HolidayOutput;
import org.slf4j.Logger;

/**
 * Provides complex dependency construction in support of test cases.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class ObjectMother {

	public static final String CENTRAL_BANK_ID_USFR = "USFR";
	public static final String CENTRAL_BANK_ID_BOE = "BoE";
	public static final String CENTRAL_BANK_ID_CBOA = "CBoA";

	public static final String HOLIDAY_DEF_ID_NEW_YEARS_DAY = "NewYearsDay";
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
		return createCentralBank(CENTRAL_BANK_ID_USFR);
	}

	public static CentralBank createCentralBank(String cbId) {
		CentralBank cb = null;
		if (CENTRAL_BANK_ID_BOE.equals(cbId)) {
			cb = new CentralBank("BoE", "Bank of England", "UK", Currency.getInstance("GBP"));
		} else if (CENTRAL_BANK_ID_USFR.equals(cbId)) {
			cb = new CentralBank("USFR", "United States Federal Reserve", "US", Currency.getInstance("USD"));
		} else if (CENTRAL_BANK_ID_CBOA.equals(cbId)) {
			cb = new CentralBank("CBoA", "Central Bank of Argentina", "AR", Currency.getInstance("ARS"));
		} else {
			throw new IllegalArgumentException("Unknown central bank ID: " + cbId);
		}
		return cb;
	}

	public static HolidayDefinition createHolidayDefinition(String defId) {
		HolidayDefinition hd = null;
		if (HOLIDAY_DEF_ID_NEW_YEARS_DAY.equals(defId)) {
			hd = new HolidayDefinition("NewYearsDay",
					                   "New Year's Day",
					                   "New Year's Day",
					                   HolidayType.FIXED,
					                   "JANUARY/01");
		}else if (HOLIDAY_DEF_ID_MLK_DAY.equals(defId)) {
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

	public static HolidayOutput<Logger, Exception> createLoggerHolidayOutput(final Logger logger) {
		return new HolidayOutput<Logger, Exception>() {
			public void store(Holiday... holidays) throws Exception {
				for (Holiday holiday : holidays) {
					HolidayId key = holiday.getId();
					logger.debug(key.toString());
				}
			}
			public void close() throws Exception {}
		};
	}

	public static URL loadDefinitionsXML() {
		return ObjectMother.class.getResource("/definitions-test.xml");
	}

}
