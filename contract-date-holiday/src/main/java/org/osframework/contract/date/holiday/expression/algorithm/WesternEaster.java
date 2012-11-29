/**
 * File: WesternEaster.java
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
package org.osframework.contract.date.holiday.expression.algorithm;

import java.util.Date;

/**
 * Provides computation of Easter Sunday by the "Western" definition (ie, Roman
 * Catholic and most Protestant churches). British, European, and some Asian
 * markets use this date for observance of Easter-relative bank holidays.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class WesternEaster implements HolidayAlgorithm {

	public static final String NAME = "EASTER";

	/**
	 * Constructor.
	 */
	public WesternEaster() {}

	/**
	 * {@inheritDoc}
	 */
	public String name() {
		return NAME;
	}

	/**
	 * Compute the date of Easter Sunday for the given year.
	 * 
	 * @param year year for which this algorithm must compute the holiday date
	 * @return date of Easter Sunday in the given year
	 */
	public Date compute(int year) {
		// TODO Auto-generated method stub
		return null;
	}

}
