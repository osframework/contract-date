/*
 * File: HolidayAlgorithm.java
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
package org.osframework.contract.date.fincal.definition.algorithm;

import java.util.Date;

/**
 * Behavior provided by objects which encapsulate a particular algorithm or
 * formula for calculation of a named holiday.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayAlgorithm {

	/**
	 * @return name of algorithm implemented by this object. Cannot be null.
	 */
	public String name();

	/**
	 * Compute the date of this object's named holiday via its algorithm.
	 * 
	 * @param year year for which this algorithm must compute the holiday date
	 * @return date of a defined holiday in the given year
	 */
	public Date compute(int year);

}
