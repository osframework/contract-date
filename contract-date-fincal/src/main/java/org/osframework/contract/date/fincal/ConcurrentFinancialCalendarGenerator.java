/*
 * File: ConcurrentFinancialCalendarGenerator.java
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
 * Provides generation of financial calendar holidays by concurrent production
 * of holidays from an array of <code>FinancialCalendar</code> objects.
 * Instances of this class perform holiday production and storage in separate
 * threads.
 * <p>Instances of this class are used in scenarios where a large number of
 * calendars and/or range of years has been requested. Holiday production for
 * each <code>FinancialCalendar</code> object is performed in a dedicated thread
 * from a fixed-size pool, with produced <code>Holiday</code> objects tranferred
 * to an output thread.</p> 
 * <p>Both this class and its single constructor method are package-private;
 * instantiation of this class is performed via the
 * {@link FinancialCalendarGeneratorBuilder#build()} method.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ConcurrentFinancialCalendarGenerator extends AbstractFinancialCalendarGenerator {


	/**
	 * Construct a concurrent, multi-threaded financial calendar generator.
	 *
	 * @param builder builder which constructs this object
	 */
	ConcurrentFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
	}

	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		// TODO Write this
	}

}
