/*
 * File: SequentialFinancialCalendarGenerator.java
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.osframework.contract.date.fincal.data.HolidayOutput;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.producer.SingleFinancialCalendarProducer;

/**
 * Provides generation of financial calendar holidays by sequential production
 * of holidays from an array of <code>FinancialCalendar</code> objects.
 * Instances of this class perform holiday production and storage in a single
 * thread.
 * <p>Both this class and its constructor method are package-private;
 * instantiation of this class is performed via the
 * {@link FinancialCalendarGeneratorBuilder#build()} method.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class SequentialFinancialCalendarGenerator extends AbstractFinancialCalendarGenerator {

	/**
	 * Construct a sequential, single-threaded financial calendar generator.
	 *
	 * @param builder builder which constructs this object
	 */
	SequentialFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		final List<Holiday> holidayQueue = new LinkedList<Holiday>();
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
			throw new FinancialCalendarException(e.getMessage(), e);
		}
	}

}