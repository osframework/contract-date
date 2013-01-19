/*
 * File: FinancialCalendarGeneratorBuilder.java
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

import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.config.Configuration;

/**
 * Builds an appropriate <code>FinancialCalendarGenerator</code> object from
 * incrementally specified parameters.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FinancialCalendarGeneratorBuilder {

	private static final int MIN_YEAR = 1900;
	private static final int MAX_YEAR = 4099;
	private static final int MEDIAN_ANNUAL_WEEKEND_DAYS = 105;
	private static final double THRESHOLD = 1000;

	final Configuration configuration;
	SortedSet<String> calendarIds;
	int firstYear, lastYear;
	boolean weekends;

	/**
	 * Construct a <code>FinancialCalendarGenerator</code> builder. Fields are
	 * initialized to sensible defaults. 
	 *
	 * @param configuration financial calendar configuration
	 */
	public FinancialCalendarGeneratorBuilder(final Configuration configuration) {
		Validate.notNull(configuration, "Configuration argument cannot be null");
		this.configuration = configuration;
		this.calendarIds = new TreeSet<String>();
		Calendar c = Calendar.getInstance();
		this.firstYear = this.lastYear = c.get(Calendar.YEAR);
		this.weekends = false;
	}

	public FinancialCalendarGeneratorBuilder addCalendar(String calendarId) {
		checkCalendar(calendarId);
		this.calendarIds.add(calendarId);
		return this;
	}

	public FinancialCalendarGeneratorBuilder setFirstYear(int firstYear) {
		checkYear(firstYear);
		this.firstYear = firstYear;
		return this;
	}

	public FinancialCalendarGeneratorBuilder setLastYear(int lastYear) {
		checkYear(lastYear);
		this.lastYear = lastYear;
		return this;
	}

	public FinancialCalendarGeneratorBuilder includeWeekends(boolean weekends) {
		this.weekends = weekends;
		return this;
	}

	public FinancialCalendarGenerator build() throws FinancialCalendarException {
		if (calendarIds.isEmpty()) {
			throw new FinancialCalendarException("No financial calendars specified");
		}
		// Ensure first and last years are in correct order prior to
		// construction
		this.firstYear = Math.min(this.firstYear, this.lastYear);
		this.lastYear = Math.max(this.firstYear, this.lastYear);
		FinancialCalendarGenerator generator = null;
		switch (calendarIds.size()) {
		case 1:
			generator = new SequentialFinancialCalendarGenerator(this);
			break;
		default:
			double estimatedTotal = calculateYearGeneratedAvg() * ((lastYear - firstYear) + 1);
			generator = (THRESHOLD <= estimatedTotal)
					    ? new ConcurrentFinancialCalendarGenerator(this)
			            : new SequentialFinancialCalendarGenerator(this);
			break;
		}
		return generator;
	}

	private void checkCalendar(String calendarId) {
		Validate.isTrue(!StringUtils.isBlank(calendarId), "Financial calendar ID cannot be blank");
		if (null == configuration.getFinancialCalendar(calendarId)) {
			throw new IllegalArgumentException("Unknown financial calendar: " + calendarId);
		}
	}

	private void checkYear(int year) {
		if (MIN_YEAR > year) {
			throw new IllegalArgumentException("First year cannot be prior to " + MIN_YEAR);
		}
		if (MAX_YEAR < year) {
			throw new IllegalArgumentException("Last year cannot be after " + MAX_YEAR);
		}
	}

	private double calculateYearGeneratedAvg() {
		final String[] avgCalendarIds;
		final int count, adjustment;
		synchronized (this) {
			avgCalendarIds = calendarIds.toArray(new String[0]);
			count = avgCalendarIds.length;
			adjustment = (weekends) ? MEDIAN_ANNUAL_WEEKEND_DAYS : 0;
		}
		int total = 0;
		for (String calendarId : avgCalendarIds) {
			total += (configuration.getFinancialCalendar(calendarId).size() + adjustment);
		}
		return (double)(total / count);
	}

}
