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

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

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
	 * ProducerWorker description here.
	 *
	 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
	 */
	class ProducerWorker implements Runnable {
		private final FinancialCalendar[] calendars;
		private final int[] years;
		private final boolean weekends;
		private final BlockingQueue<Holiday> targetChannel;
		private final CyclicBarrier barrier;
	
		private volatile Throwable error;
	
		ProducerWorker(final FinancialCalendar[] calendars,
				       final int[] years,
				       final boolean weekends,
				       final BlockingQueue<Holiday> targetChannel,
				       final CyclicBarrier barrier) {
			this.calendars = calendars;
			this.years = years;
			this.weekends = weekends;
			this.targetChannel = targetChannel;
			this.barrier = barrier;
		}
	
		@Override
		public void run() {
			Arrays.sort(years);
			HolidayProducer<FinancialCalendar> producer = null;
			try {
				for (int i = 0; i < years.length; i++) {
					producer = new SingleYearProducer(years[i], weekends);
					Holiday[] produced = producer.produce(calendars);
					for (Holiday h : produced) {
						targetChannel.put(h);
					}
				}
				// Done; await at barrier
				barrier.await();
			} catch (BrokenBarrierException bbe) {
				// Don't care -- controller thread will get
				// BrokenBarrierException
			} catch (InterruptedException ie) {
				// Don't care -- controller thread will get
				// BrokenBarrierException
			} catch (Throwable t) {
				error = t;
				Thread.currentThread().interrupt();
				try {
					barrier.await();
				} catch (Exception e) {}
			}
		}
	}

	static enum WorkDivider {
		CALENDAR,
		YEAR;
	
		public static WorkDivider coinToss() {
			Random r = new Random();
			int ordinal = r.nextInt(2);
			for (WorkDivider wd : values()) {
				if (wd.ordinal() == ordinal) {
					return wd;
				}
			}
			throw new Error("Coin toss produced invalid ordinal");
		}
	}

	private volatile RuntimeException exceptionInPostRunnable;

	/**
	 * Construct a concurrent, multi-threaded financial calendar generator.
	 *
	 * @param builder builder which constructs this object
	 */
	ConcurrentFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		exceptionInPostRunnable = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		// Channels
		final BlockingQueue<Holiday> producedChannel = new LinkedBlockingQueue<Holiday>();
		final SortedSet<Holiday> sortBuffer = Collections.synchronizedSortedSet(new TreeSet<Holiday>());
		
		// Allocate 1 thread per processor
		final int threadCount = Runtime.getRuntime().availableProcessors();
		
		final CyclicBarrier barrier = new CyclicBarrier((threadCount + 1), new Runnable() {
			public void run() {
				try {
					synchronized (sortBuffer) {
						producedChannel.drainTo(sortBuffer);
					}
				} catch (RuntimeException re) {
					exceptionInPostRunnable = re;
					throw re;
				}
			}
		});
		
		final ProducerWorker[] producerWorkers = createProducerWorkers(threadCount, producedChannel, barrier);
		Thread t = null;
		for (ProducerWorker pw : producerWorkers) {
			t = new Thread(pw);
			t.start();
		}
		try {
			barrier.await();
			// Check for exceptions in worker threads
			for (int i = 0; i < producerWorkers.length; i++) {
				if (null != producerWorkers[i].error) {
					throw new FinancialCalendarException("Worker[" + i + "] terminated unexpectedly", producerWorkers[i].error);
				}
			}
			// Check for exception in barrier post-action
			if (null != exceptionInPostRunnable) {
				throw exceptionInPostRunnable;
			}
			synchronized (sortBuffer) {
				output.store(sortBuffer.toArray(EMPTY_HOLIDAY_ARRAY));
			}
		} catch (Exception e) {
			FinancialCalendarException fce = (e instanceof FinancialCalendarException)
					                          ? ((FinancialCalendarException)e)
					                          : new FinancialCalendarException("Holiday output failed", e);
			throw fce;
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				throw new FinancialCalendarException("Cannot close holiday output target", e);
			}
		}
	}

	private ProducerWorker[] createProducerWorkers(final int threadCount,
			                                       final BlockingQueue<Holiday> channel,
			                                       final CyclicBarrier barrier) throws FinancialCalendarException {
		// Create 1 producer worker per thread
		final ProducerWorker[] workers = new ProducerWorker[threadCount];
		
		// Load calendar array from configuration
		final FinancialCalendar[] calendars = getFinancialCalendars();
		// Load full array of contiguous years (inclusive range)
		final int[] years = getYears();
		// Divide work by the largest range
		final WorkDivider divideBy = (years.length > calendars.length)
				                      ? WorkDivider.YEAR
				                      : ((calendars.length > years.length) ? WorkDivider.CALENDAR : WorkDivider.coinToss());
		int r, chunkSize, offset, arraySize;
		switch (divideBy) {
		case CALENDAR:
			// Divide calendars into <threadCount> chunks
			// Last segment will contain remaining calendars, if any
			r = calendars.length % threadCount;
			chunkSize = calendars.length / threadCount;
			FinancialCalendar[][] calendarChunks = new FinancialCalendar[threadCount][];
			for (int i = 0; i < threadCount; i++) {
				offset = i * chunkSize;
				arraySize = (i == (threadCount - 1)) ? (chunkSize + r) : chunkSize;
				calendarChunks[i] = new FinancialCalendar[arraySize];
				System.arraycopy(calendars, offset, calendarChunks[i], 0, arraySize);
				workers[i] = new ProducerWorker(calendarChunks[i], years, this.weekends, channel, barrier);
			}
			break;
		case YEAR:
		default:
			// Divide years into <threadCount> chunks
			// Last segment will contain remaining years, if any
			r = years.length % threadCount;
			chunkSize = years.length / threadCount;
			int[][] yearChunks = new int[threadCount][];
			for (int i = 0; i < threadCount; i++) {
				offset = i * chunkSize;
				arraySize = (i == (threadCount - 1)) ? (chunkSize + r) : chunkSize;
				yearChunks[i] = new int[arraySize];
				System.arraycopy(years, offset, yearChunks[i], 0, arraySize);
				workers[i] = new ProducerWorker(calendars, yearChunks[i], this.weekends, channel, barrier);
			}
			break;
		}
		
		return workers;
	}

}
