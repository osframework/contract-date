/*
 * File: AbstractHolidayOutputWriter.java
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
package org.osframework.contract.date.fincal.output;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.source.DefinitionSource;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass of configurable <tt>HolidayOutputWriter</tt> objects.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractHolidayOutputWriter implements HolidayOutputWriter {

	protected static final Holiday[] EMPTY_HOLIDAY_ARRAY = {};
	protected static final String[] EMPTY_STRING_ARRAY = {};

	protected final Logger logger;

	protected DefinitionSource definitionSource = null;
	protected int firstYear, lastYear;
	protected boolean includeWeekends = false;
	protected Set<String> calendarIds = new HashSet<String>();

	/**
	 * Constructor. Initializes logging facility.
	 */
	public AbstractHolidayOutputWriter() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * @param definitionSource the definitionSource to set
	 */
	public void setDefinitionSource(DefinitionSource definitionSource) {
		this.definitionSource = definitionSource;
	}

	/**
	 * @param firstYear the firstYear to set
	 */
	public void setFirstYear(int firstYear) {
		this.firstYear = firstYear;
	}

	/**
	 * @param lastYear the lastYear to set
	 */
	public void setLastYear(int lastYear) {
		this.lastYear = lastYear;
	}

	public void setCalendarIds(String... calendarIds) {
		this.calendarIds.addAll(Arrays.asList(calendarIds));
	}

	public void addCalendarId(String calendarId) {
		if (!calendarIds.contains(calendarId)) {
			calendarIds.add(calendarId);
		}
	}

	public void includeWeekends() {
		this.includeWeekends = true;
	}

	public void ignoreWeekends() {
		this.includeWeekends = false;
	}

	protected final FinancialCalendar[] getFinancialCalendars() {
		String[] sorted = calendarIds.toArray(EMPTY_STRING_ARRAY);
		Arrays.sort(sorted);
		final FinancialCalendar[] calendars = new FinancialCalendar[sorted.length];
		for (int i = 0; i < sorted.length; i++) {
			calendars[i] = definitionSource.getFinancialCalendar(sorted[i]);
		}
		return calendars;
	}

	protected final Integer[] getYearRange() {
		Integer[] years = new Integer[(lastYear - firstYear) + 1];
		for (int i = 0, j = firstYear; j <= lastYear; i++, j++) {
			years[i] = Integer.valueOf(j);
		}
		return years;
	}

}
