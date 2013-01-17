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
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.producer.HolidayProducer;
import org.osframework.contract.date.fincal.producer.SingleFinancialCalendarProducer;

/**
 * SequentialFinancialCalendarGenerator description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class SequentialFinancialCalendarGenerator extends AbstractFinancialCalendarGenerator {

	/**
	 * Constructor - description here.
	 *
	 * @param builder
	 */
	SequentialFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
	}

	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		List<Holiday> holidayQueue = new LinkedList<Holiday>();
		Arrays.sort(calendarIds);
		HolidayProducer<Integer> producer;
		for (String calendarId : calendarIds) {
			producer = new SingleFinancialCalendarProducer(configuration.getFinancialCalendar(calendarId));
			holidayQueue.addAll(Arrays.asList(producer.produce(this.firstYear, this.lastYear)));
		}
		
		// TODO Handle weekend generation
		
		Collections.sort(holidayQueue);
		
		logger.debug("Storing {} total holidays generated for {} financial calendars",
				     String.valueOf(holidayQueue.size()), String.valueOf(calendarIds.length));
		try {
			Holiday[] allSorted = holidayQueue.toArray(new Holiday[0]);
			int totalSize = allSorted.length;
			output.store(allSorted);
			logger.info("Stored {} total holidays; closing output", String.valueOf(totalSize));
			output.close();
		} catch (Exception e) {
			throw new FinancialCalendarException(e.getMessage(), e);
		}
	}

}
