/**
 * File: HolidayType.java
 * 
 * Copyright 2012 David A. Joyce
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
package org.osframework.contract.date.holiday;

import org.osframework.contract.date.holiday.expression.HolidayExpression;
import org.osframework.contract.date.holiday.expression.HolidayExpressionFixedImpl;
import org.osframework.contract.date.holiday.expression.HolidayExpressionRelativeImpl;

/**
 * Enumeration of holiday types. Given a valid expression, a
 * HolidayType produces an instance of its corresponding HolidayExpression
 * implementation.
 *
 * @see HolidayExpression
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public enum HolidayType {

	/**
	 * Holiday which always falls on the same date every year.
	 */
	FIXED(new HolidayExpressionStrategy() {
		public HolidayExpression toExpression(String ruleExpression) {
			return new HolidayExpressionFixedImpl(ruleExpression);
		};
	}),

	/**
	 * Holiday which falls on the <i>N</i>th weekday of a month every year.
	 */
	RELATIVE(new HolidayExpressionStrategy() {
		public HolidayExpression toExpression(String ruleExpression) {
			return new HolidayExpressionRelativeImpl(ruleExpression);
		};
	}),

	/**
	 * Holiday for which a date must be calculated via some algorithm. Many
	 * religious / ecumenical calendar holidays are of this type.
	 */
	CALCULATED(new HolidayExpressionStrategy() {
		public HolidayExpression toExpression(String ruleExpression) {
			throw new UnsupportedOperationException("Not implemented yet");
		};
	}),

	/**
	 * Holiday which occurs exactly once (typically a national special occasion).
	 */
	ONETIME(new HolidayExpressionStrategy() {
		public HolidayExpression toExpression(String ruleExpression) {
			//return new FixedHolidayRule(ruleExpression);
			return null;
		};
	});

	private final HolidayExpressionStrategy strategy;

	private HolidayType(final HolidayExpressionStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Factory method to convert the specified string to an expression of this
	 * <code>HolidayType</code>.
	 * 
	 * @param expression string to be converted
	 * @return expression of this type which can produce the holiday date for a
	 *         given year
	 */
	public HolidayExpression toExpression(final String expression) {
		return this.strategy.toExpression(expression);
	}

	private static interface HolidayExpressionStrategy {

		HolidayExpression toExpression(final String ruleExpression);

	}

}
