/*
 * File: HolidayProducer.java
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
package org.osframework.contract.date.fincal.producer;

import org.osframework.contract.date.fincal.model.Holiday;

/**
 * Core behavior of objects which produce <code>Holiday</code> objects.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayProducer {

	/**
	 * Produce the array of <code>Holiday</code> objects for the specified
	 * years. This specification makes no requirements for the order of
	 * elements in the returned array. It is the responsibility of an
	 * implementor and/or its client to define whether the produced array is
	 * sorted or unsorted.
	 * 
	 * @param years one or more years for which to produce holidays
	 * @return array of <code>Holiday</code> objects
	 */
	public Holiday[] produce(int... years);

	final Holiday[] EMPTY_ARRAY = {};

}
