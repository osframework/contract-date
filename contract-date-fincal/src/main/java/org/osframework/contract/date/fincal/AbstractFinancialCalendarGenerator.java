/*
 * File: AbstractFinancialCalendarGenerator.java
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

import java.util.Arrays;

import org.osframework.contract.date.fincal.config.Configuration;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractFinancialCalendarGenerator description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractFinancialCalendarGenerator implements FinancialCalendarGenerator {

	protected static final Holiday[] EMPTY_HOLIDAY_ARRAY = {};
	protected static final String[] EMPTY_STRING_ARRAY = {};

	protected final Configuration configuration;
	protected final String[] calendarIds;
	protected final int firstYear;
	protected final int lastYear;
	protected final boolean weekends;
	protected final Logger logger;

	/**
	 * Protected constructor.
	 */
	protected AbstractFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.configuration = builder.configuration;
		this.calendarIds = builder.calendarIds.toArray(EMPTY_STRING_ARRAY);
		this.firstYear = builder.firstYear;
		this.lastYear = builder.lastYear;
		this.weekends = builder.weekends;
	}

	protected final FinancialCalendar[] getFinancialCalendars() throws FinancialCalendarException {
		Arrays.sort(calendarIds);
		final FinancialCalendar[] calendars = new FinancialCalendar[calendarIds.length];
		for (int i = 0; i < calendarIds.length; i++) {
			calendars[i] = configuration.getFinancialCalendar(calendarIds[i]);
			if (null == calendars[i]) {
				throw new FinancialCalendarException("Unexpected null financial calendar: " + calendarIds[i]);
			}
		}
		return calendars;
	}

}
