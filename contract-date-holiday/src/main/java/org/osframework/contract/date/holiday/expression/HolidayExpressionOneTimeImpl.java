/**
 * File: HolidayExpressionOneTimeImpl.java
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

import org.osframework.contract.date.util.DateUtil;

/**
 * Produce the date of a holiday occurring exactly one time. Examples of
 * one-time holidays include royal weddings, jubilees, etc.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class HolidayExpressionOneTimeImpl implements HolidayExpression {

	private final Date oneTimeHolidayDate;

	/**
	 * Constructor - accepts expression argument of the form:
	 * <pre>
	 * yyyy-MM-dd
	 * </pre>
	 */
	public HolidayExpressionOneTimeImpl(final String expression) {
		if (null == expression) {
			throw new IllegalArgumentException("argument 'expression' cannot be null");
		}
		if (!DateUtil.isDate(expression)) {
			throw new IllegalArgumentException("argument 'expression' must be a valid date");
		}
		this.oneTimeHolidayDate = DateUtil.parseDate(expression);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(oneTimeHolidayDate);
		int y = c.get(Calendar.YEAR);
		return (y == year) ? oneTimeHolidayDate : null;
	}

}
