/*
 * File: HolidayType.java
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
package org.osframework.contract.date.fincal.definition;

import org.osframework.contract.date.fincal.definition.expression.HolidayExpressionCalculatedImpl;
import org.osframework.contract.date.fincal.definition.expression.HolidayExpressionFixedImpl;
import org.osframework.contract.date.fincal.definition.expression.HolidayExpressionOneTimeImpl;
import org.osframework.contract.date.fincal.definition.expression.HolidayExpressionRelativeImpl;

/**
 * Enumeration of holiday types. Given a valid expression, a
 * HolidayType produces an instance of its corresponding HolidayExpression
 * implementation.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see HolidayExpression
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
			return new HolidayExpressionCalculatedImpl(ruleExpression);
		};
	}),

	/**
	 * Holiday which occurs exactly once (typically a national special occasion).
	 */
	ONETIME(new HolidayExpressionStrategy() {
		public HolidayExpression toExpression(String ruleExpression) {
			return new HolidayExpressionOneTimeImpl(ruleExpression);
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
	 * @throws IllegalArgumentException if expression is null or invalid
	 */
	public HolidayExpression toExpression(final String expression) {
		return this.strategy.toExpression(expression);
	}

	private static interface HolidayExpressionStrategy {

		HolidayExpression toExpression(final String ruleExpression);

	}

}
