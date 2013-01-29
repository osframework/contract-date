/*
 * File: ConcurrentFinancialCalendarRangeGenerator.java
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

import org.osframework.contract.date.fincal.data.HolidayOutput;
import org.osframework.contract.date.fincal.model.FinancialCalendar;

/**
 * ConcurrentFinancialCalendarRangeGenerator description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ConcurrentFinancialCalendarRangeGenerator extends AbstractFinancialCalendarGenerator {

	private final int workerCount;
	private final int bucketSize;
	private final FinancialCalendar[][] buckets;

	/**
	 * Constructor - description here.
	 *
	 * @param builder
	 */
	public ConcurrentFinancialCalendarRangeGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		final int procCount = Runtime.getRuntime().availableProcessors();
		this.bucketSize = (calendars.length > procCount)
				           ? Double.valueOf(Math.ceil((double)calendars.length / (double)procCount)).intValue() : 1;
		this.workerCount = (calendars.length >= procCount) ? procCount : calendars.length;
		this.buckets = new FinancialCalendar[workerCount][bucketSize];
	}

	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		// TODO Auto-generated method stub

	}

}
