/*
 * File: HolidayOutputWriter.java
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
package org.osframework.contract.date.fincal.output;


/**
 * Defines the required behavior of objects which write financial calendar
 * holidays to an output destination.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayOutputWriter {

	/**
	 * Generate financial calendar holidays and write them to the specified
	 * output.
	 * 
	 * @param output holiday output target
	 * @throws HolidayOutputException if holiday generation or output fails
	 *         for any reason
	 */
	public void writeHolidays(HolidayOutput<?, ?> output)
		throws HolidayOutputException;

}
