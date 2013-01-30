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

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import org.osframework.contract.date.fincal.data.HolidayOutput;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.producer.SingleYearProducer;

/**
 * Provides generation of financial calendar holidays by concurrent production
 * of holidays from an inclusive range of years. The range of years is
 * distributed into <i>N</i> buckets, where <i>N</i> is the number of processors
 * available to the JVM. For each bucket / processor, a dedicated producer
 * thread is allocated. The range of years for which holidays are generated is
 * defined as <code>(lastYear - firstYear) + 1</code>.
 * <p>Both this class and its constructor method are package-private;
 * instantiation of this class is performed via the
 * {@link FinancialCalendarGeneratorBuilder#build()} method.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class ConcurrentYearRangeGenerator extends AbstractFinancialCalendarGenerator {

	class ProducerThread extends Thread {
	
		final int id;
	
		public ProducerThread(final int id) {
			super();
			this.id = id;
		}
		
		@Override
		public void run() {
			try {
				Integer[] years = buckets[id];
				for (int i = 0; i < years.length; i++) {
					SingleYearProducer syp = new SingleYearProducer(years[i], weekends);
					Holiday[] holidays = syp.produce(calendars);
					for (Holiday holiday : holidays) {
						queue.put(holiday);
					}
				}
				barrier.await();
			} catch (BrokenBarrierException bbe) {
				logger.error("Controller thread will receive BrokenBarrierException; ignoring...");
			} catch (InterruptedException ie) {
				logger.error("Controller thread will receive BrokenBarrierException; ignoring...");
			} catch (Throwable t) {
				Thread.currentThread().interrupt();
				try {
					barrier.await();
				} catch (Exception e) {
					logger.error("Cannot await on barrier; initial cause: ", t);
				}
			}
		}
	}

	private final int workerCount;
	private final Integer[][] buckets;
	private final CyclicBarrier barrier;
	private final BlockingQueue<Holiday> queue;


	/**
	 * Package-private constructor. Called exclusively by the specified builder
	 * which instantiates objects of this class.
	 * 
	 * @param builder builder instantiating this generator object
	 */
	ConcurrentYearRangeGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		final int procCount = Runtime.getRuntime().availableProcessors();
		final int y = years.length;
		if (y < procCount) {
			this.workerCount = y;
			this.buckets = new Integer[workerCount][1];
			for (int i = y; i < y; i++) {
				buckets[i][0] = years[i];
			}
		} else {
			this.workerCount = procCount;
			this.buckets = new Integer[workerCount][];
			int r = y % procCount,
				q = (y - r) / procCount,
				start = 0;
			for (int i = 0; i < workerCount; i++) {
				int sz;
				if (0 < r) {
					sz = q+1;
					r -= 1;
				} else {
					sz = q;
				}
				buckets[i] = new Integer[sz];
				System.arraycopy(years, start, buckets[i], start, sz);
				start += sz;
			}
		}
		this.barrier = new CyclicBarrier(workerCount + 1);
		this.queue = new LinkedBlockingQueue<Holiday>();
	}

	@Override
	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		for (int w = 0; w < workerCount; w++) {
			ProducerThread pt = new ProducerThread(w);
			pt.start();
		}
		try {
			barrier.await();
			final SortedSet<Holiday> outputQueue = new TreeSet<Holiday>();
			queue.drainTo(outputQueue);
			output.store(outputQueue.toArray(EMPTY_HOLIDAY_ARRAY));
		} catch (Exception e) {
			throw new FinancialCalendarException("Cannot store generated holidays to output", e);
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				logger.error("Cannot close HolidayOutput", e);
			}
		}
	}

}
