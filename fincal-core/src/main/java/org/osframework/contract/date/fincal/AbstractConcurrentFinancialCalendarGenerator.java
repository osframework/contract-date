/*
 * File: AbstractConcurrentFinancialCalendarGenerator.java
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

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osframework.contract.date.fincal.data.HolidayOutput;
import org.osframework.contract.date.fincal.model.Holiday;


/**
 * AbstractConcurrentFinancialCalendarGenerator description here.
 *
 * @param <T> element type of producer data buckets
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
abstract class AbstractConcurrentFinancialCalendarGenerator<T> extends AbstractFinancialCalendarGenerator {

	abstract class Producer implements Runnable {
		final int id;
	
		Producer(final int id) {
			this.id = id;
		}
	
		public void run() {
			rwl.readLock().lock();
			try {
				generateHolidays(producerBuckets.get(id));
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			} finally {
				producersRunning.decrementAndGet();
				rwl.readLock().unlock();
			}
		}
	
		abstract void generateHolidays(final List<T> myBucket) throws Exception;
	
	}

	class Consumer implements Runnable {
		final HolidayOutput<?, ?> output;
	
		Consumer(final HolidayOutput<?, ?> output) {
			this.output = output;
		}
	
		public void run() {
			try {
				rwl.writeLock().lockInterruptibly();
				while (0 < producersRunning.get()) {
					output.store(queue.take());
				}
				if (!queue.isEmpty()) {
					SortedSet<Holiday> remaining = new TreeSet<Holiday>();
					queue.drainTo(remaining);
					output.store(remaining.toArray(EMPTY_HOLIDAY_ARRAY));
					queue.clear();
					remaining.clear();
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			} finally {
				try {
					output.close();
				} catch (Exception e) {
					
				}
				rwl.writeLock().unlock();
			}
		}
	}

	protected final BlockingQueue<Holiday> queue;
	protected final int producerCount;
	protected final List<List<T>> producerBuckets;
	protected final ReadWriteLock rwl;
	protected final AtomicInteger producersRunning;

	/**
	 * Protected constructor.
	 */
	protected AbstractConcurrentFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		this.queue = createHolidayQueue();
		this.producerCount = calculateProducerCount();
		this.producerBuckets = createProducerBuckets();
		this.rwl = new ReentrantReadWriteLock();
		this.producersRunning = new AtomicInteger(producerCount);
	}

	BlockingQueue<Holiday> createHolidayQueue() {
		return new PriorityBlockingQueue<Holiday>();
	}

	int calculateProducerCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	abstract List<List<T>> createProducerBuckets();

	abstract Producer createProducer(final int producerId);

	public void generateHolidays(HolidayOutput<?, ?> output) throws FinancialCalendarException {
		final ExecutorService consumerService = Executors.newSingleThreadExecutor();
		consumerService.submit(new Consumer(output));
		
		logger.info("Splitting holiday generation across {} producers", producersRunning);
		final ExecutorService producerService = Executors.newFixedThreadPool(producerCount);
		for (int i = 0; i < producerCount; i++) {
			producerService.submit(createProducer(i));
		}
		
		producerService.shutdown();
		try {
			producerService.awaitTermination(500, TimeUnit.MILLISECONDS);
			consumerService.shutdown();
			consumerService.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {
			throw new FinancialCalendarException("Producer/Consumer executor thread interrupted", ie);
		} finally {
			if (!producerService.isShutdown()) {
				producerService.shutdownNow();
			}
			if (!consumerService.isShutdown()) {
				consumerService.shutdownNow();
			}
			logger.info("All holiday generation stopped");
		}
	}

}
