/**
 * File: USFederalReserveDecorator.java
 * 
 * Copyright 2012 David A. Joyce, OSFramework Project.
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
package org.osframework.contract.date.holiday.expression;

import java.util.Calendar;
import java.util.Date;

/**
 * USFederalReserveDecorator description here.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 * @see <a href="http://www.federalreserve.gov/aboutthefed/k8.htm">K.8 - Holidays Observed by the Federal Reserve Board</a>
 */
public class USFederalReserveDecorator extends HolidayExpressionDecorator {

	/**
	 * @param decoratedExpression
	 */
	public USFederalReserveDecorator(HolidayExpression decoratedExpression) {
		super(decoratedExpression);
	}

	/**
	 * Produce the date of a defined holiday for the specified year. Per Federal
	 * Reserve Board guidelines, this implementation adjusts the holiday date 1
	 * day forward to the Monday, if the actual holiday date falls on a Sunday.
	 * 
	 * @param year year for which this expression must produce the holiday date
	 * @return date of a defined holiday in the given year
	 */
	@Override
	public Date evaluate(int year) {
		// TODO Auto-generated method stub
		Date d = super.evaluate(year);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if (Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return c.getTime();
	}

}
