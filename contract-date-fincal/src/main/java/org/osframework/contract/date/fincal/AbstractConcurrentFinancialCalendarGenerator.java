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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osframework.contract.date.fincal.model.Holiday;


/**
 * AbstractConcurrentFinancialCalendarGenerator description here.
 *
 * @param <T> element type of producer data buckets
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
abstract class AbstractConcurrentFinancialCalendarGenerator<T> extends AbstractFinancialCalendarGenerator {

	protected final BlockingQueue<Holiday> queue;
	protected final int producerCount;
	protected final List<List<T>> producerBuckets;
	protected final ReadWriteLock rwl;

	/**
	 * Protected constructor.
	 */
	protected AbstractConcurrentFinancialCalendarGenerator(FinancialCalendarGeneratorBuilder builder) {
		super(builder);
		this.queue = createHolidayQueue();
		this.producerCount = calculateProducerCount();
		this.producerBuckets = createProducerBuckets();
		this.rwl = new ReentrantReadWriteLock();
	}

	abstract BlockingQueue<Holiday> createHolidayQueue();

	abstract int calculateProducerCount();

	abstract List<List<T>> createProducerBuckets();

}
