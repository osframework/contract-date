/*
 * File: SequentialHolidayOutputWriter.java
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.osframework.contract.date.fincal.holiday.producer.SingleFinancialCalendarProducer;

/**
 * Provides writing of holidays produced in sequence from an array of selected
 * <tt>FinancialCalendar</tt> objects. Instances of this class perform holiday
 * production and storage in a single thread.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SequentialHolidayOutputWriter extends AbstractHolidayOutputWriter {

	/**
	 * Constructor.
	 */
	public SequentialHolidayOutputWriter() {
		super();
	}

	public void writeHolidays(HolidayOutput<?, ?> output) throws HolidayOutputException {
		final List<Holiday> holidayQueue = new LinkedList<Holiday>();
		FinancialCalendar[] calendars = null;
		Integer[] years = null;
		final boolean weekends = includeWeekends;
		try {
			calendars = getFinancialCalendars();
			years = getYearRange();
			if (null == calendars || 0 == calendars.length) {
				throw new NullPointerException("Missing specified calendars");
			}
			if (null == years || 0 == years.length) {
				throw new NullPointerException("Missing required year range");
			}
		} catch (Exception e) {
			throw new HolidayOutputException("Missing required parameters prior to holiday output", e);
		}
		
		for (FinancialCalendar calendar : calendars) {
			holidayQueue.addAll(
				Arrays.asList(
					new SingleFinancialCalendarProducer(calendar, weekends).produce(years)
				)
			);
		}
		Collections.sort(holidayQueue);
		
		logger.debug("Storing {} total holidays generated for {} financial calendars",
			         String.valueOf(holidayQueue.size()), String.valueOf(calendars.length));
		try {
			Holiday[] allSorted = holidayQueue.toArray(EMPTY_HOLIDAY_ARRAY);
			int totalSize = allSorted.length;
			output.store(allSorted);
			logger.info("Stored {} total holidays; closing output", String.valueOf(totalSize));
			output.close();
		} catch (Exception e) {
			throw new HolidayOutputException(e.getMessage(), e);
		}
	}

}
