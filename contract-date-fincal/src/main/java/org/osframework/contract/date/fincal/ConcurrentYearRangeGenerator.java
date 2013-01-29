/*
 * File: ConcurrentYearRangeGenerator.java
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

/**
 * ConcurrentYearRangeGenerator description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ConcurrentYearRangeGenerator extends AbstractFinancialCalendarGenerator {

	private final int workerCount;

	/**
	 * Constructor - description here.
	 *
	 * @param builder
	 */
	public ConcurrentYearRangeGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		this.workerCount = Runtime.getRuntime().availableProcessors();
	}

	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		// TODO Auto-generated method stub

	}

}
