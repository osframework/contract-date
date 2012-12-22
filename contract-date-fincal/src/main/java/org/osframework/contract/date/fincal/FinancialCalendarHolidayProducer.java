/*
 * File: FinancialCalendarHolidayProducer.java
 * 
 * Copyright 2012 OSFramework Project.
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

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.math.Range;
import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.expression.centralbank.CentralBankDecoratorLocator;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Produces holiday dates in a range of years for a single
 * <code>FinancialCalendar</code>. The generated holidays are enqueued for later
 * write out to an output destination.
 * <p>Instances of this class are designed to work concurrently in a
 * multi-threaded context.</p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class FinancialCalendarHolidayProducer implements Runnable {

	private final FinancialCalendar calendar;
	private final Range years;
	private final Queue<Holiday> queue;
	private final CountDownLatch startSignal, doneSignal;

	/**
	 * Constructor - description here.
	 *
	 */
	public FinancialCalendarHolidayProducer(final FinancialCalendar calendar,
			                                final Range years,
			                                final Queue<Holiday> queue,
			                                final CountDownLatch startSignal,
			                                final CountDownLatch doneSignal) {
		this.calendar = calendar;
		this.years = years;
		this.queue = queue;
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	public void run() {
		try {
			startSignal.await();
			produceHolidays();
			doneSignal.countDown();
		} catch (InterruptedException ie) {
			return;
		}
	}

	void produceHolidays() {
		int startYear = years.getMinimumInteger();
		int endYear = years.getMaximumInteger();
		HolidayExpression expr;
		Date d;
		for (int y = startYear; y <= endYear; y++) {
			for (HolidayDefinition def : calendar) {
				expr = CentralBankDecoratorLocator.decorate(def.createHolidayExpression(), calendar.getCentralBank());
				d = expr.evaluate(y);
				if (null == d) {
					continue;
				}
				queue.offer(new Holiday(calendar, d, def));
			}
		}
	}

}
