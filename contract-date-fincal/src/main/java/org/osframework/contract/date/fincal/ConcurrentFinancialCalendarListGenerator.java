/*
 * File: ConcurrentFinancialCalendarListGenerator.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.producer.SingleFinancialCalendarProducer;

/**
 * Provides generation of financial calendar holidays by concurrent production
 * of holidays from an array of <code>FinancialCalendar</code> objects. The
 * array of <code>FinancialCalendar</code> objects is distributed into <i>N</i>
 * buckets, where <i>N</i> is the number of processors available to the JVM. For
 * each bucket / processor, a dedicated producer thread is allocated.
 * <p>Both this class and its constructor method are package-private;
 * instantiation of this class is performed via the
 * {@link FinancialCalendarGeneratorBuilder#build()} method.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ConcurrentFinancialCalendarListGenerator extends AbstractConcurrentFinancialCalendarGenerator<FinancialCalendar> {

	class FinancialCalendarBucketProducer extends Producer {
	
		FinancialCalendarBucketProducer(final int id) {
			super(id);
		}
	
		void generateHolidays(List<FinancialCalendar> myBucket) throws Exception {
			for (FinancialCalendar calendar : myBucket) {
				Holiday[] holidays = new SingleFinancialCalendarProducer(calendar, weekends).produce(years);
				for (Holiday h : holidays) {
					queue.put(h);
				}
			}
		}
	
	}

	/**
	 * Construct a concurrent, multi-threaded financial calendar generator.
	 *
	 * @param builder builder which constructs this object
	 */
	ConcurrentFinancialCalendarListGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
	}

	@Override
	int calculateProducerCount() {
		final int procCount = Runtime.getRuntime().availableProcessors();
		return Math.min(calendars.length, procCount);
	}

	Producer createProducer(int producerId) {
		return new FinancialCalendarBucketProducer(producerId);
	}

	List<List<FinancialCalendar>> createProducerBuckets() {
		final List<FinancialCalendar> calendarList = Arrays.asList(calendars);
		final List<List<FinancialCalendar>> buckets = new ArrayList<List<FinancialCalendar>>();
		final int remainder = calendarList.size() % producerCount,
				  bucketSize = (calendarList.size() - remainder) / producerCount;
		for (int b = 0, r = remainder; b < producerCount; b++, r--) {
			int adjSize = bucketSize + ((0 < r) ? 1 : 0);
			buckets.add(calendarList.subList((b * adjSize), (((b+1) * adjSize) - 1)));
		}
		return buckets;
	}

}
