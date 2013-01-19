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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.osframework.contract.date.fincal.data.HolidayOutput;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.producer.HolidayProducer;
import org.osframework.contract.date.fincal.producer.SingleYearProducer;

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
		// Load calendar array from configuration
		final FinancialCalendar[] calendars = new FinancialCalendar[calendarIds.length];
		int i;
		for (i = 0; i < calendarIds.length; i++) {
			calendars[i] = configuration.getFinancialCalendar(calendarIds[i]);
			if (null == calendars[i]) {
				throw new FinancialCalendarException("Unexpected null financial calendar: " + calendarIds[i]);
			}
		}
		
		final int producerCount = calendars.length;
		final ExecutorService producerService = Executors.newFixedThreadPool(producerCount),
				              consumerService = Executors.newSingleThreadExecutor();
		final BlockingQueue<Holiday> producedChannel = new LinkedBlockingQueue<Holiday>();
		final BlockingQueue<Holiday> outputChannel = new SynchronousQueue<Holiday>();
		
		// TODO Start consumer for storage to output
		
		// CyclicBarrier sync point for all producer threads
		final CyclicBarrier producerSyncPoint = new CyclicBarrier(producerCount, getSequencer(producedChannel, outputChannel));
		for (int year = firstYear; year <= lastYear; year++) {
			for (int producerNumber = 0; producerNumber < producerCount; producerNumber++) {
				producerService.execute(new ProducerWorker(calendars[producerNumber], year, producedChannel, producerSyncPoint));
			}
		}
	}

	private Runnable getSequencer(final BlockingQueue<Holiday> inChannel,
			                      final BlockingQueue<Holiday> outChannel) {
		return new Runnable() {
			public void run() {
				final SortedSet<Holiday> sortBuffer = new TreeSet<Holiday>();
				inChannel.drainTo(sortBuffer);
				for (Holiday holiday : sortBuffer) {
					try {
						outChannel.put(holiday);
					} catch (InterruptedException ie) {
						// If put is interrupted, ensure this thread interrupts
						Thread.currentThread().interrupt();
					}
				}
			}
		};
	}

	private class ProducerWorker implements Runnable {
		private final FinancialCalendar calendar;
		private final HolidayProducer<FinancialCalendar> producer;
		private final BlockingQueue<Holiday> targetChannel;
		private final CyclicBarrier syncPoint;
	
		private volatile Throwable error;
	
		ProducerWorker(final FinancialCalendar calendar,
				       final int year,
				       final BlockingQueue<Holiday> targetChannel,
				       final CyclicBarrier syncPoint) {
			this.calendar = calendar;
			this.producer = new SingleYearProducer(year);
			this.targetChannel = targetChannel;
			this.syncPoint = syncPoint;
		}
	
		public void run() {
			try {
				Holiday[] holidays = producer.produce(calendar);
				// TODO Produce weekends here
				for (Holiday holiday : holidays) {
					targetChannel.put(holiday);
				}
				syncPoint.await();
			} catch (InterruptedException ie) {
				// Don't care; controller thread will get BrokenBarrierException
			} catch (BrokenBarrierException bbe) {
				// Don't care; controller thread will get BrokenBarrierException
			} catch (Throwable t) {
				// Some other exception occurred during our task
				error = t;
				Thread.currentThread().interrupt();
				try {
					syncPoint.await();
				} catch (Exception e) {}
			}
		}
	}

}
