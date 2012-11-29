/**
 * File: HolidayExpressionCalculatedImpl.java
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
package org.osframework.contract.date.holiday.expression;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osframework.contract.date.holiday.expression.algorithm.HolidayAlgorithm;
import org.osframework.contract.date.holiday.expression.algorithm.HolidayAlgorithmLoader;

/**
 * Produce the date of a holiday calculated via an algorithm and/or formula.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class HolidayExpressionCalculatedImpl implements HolidayExpression {

	public static final String REGEX_CALCULATED_EXPRESSION = "^([a-zA-Z0-9_]+)((\\+|-)(\\d+))?$";

	private final HolidayAlgorithm algorithm;
	private final int daysFromCalculated;

	/**
	 * Constructor - accepts expression argument of the forms:
	 * <ol type="A">
	 * 	<li><code>HOLIDAY_ALGORITHM_NAME</code>, or</li>
	 * 	<li><code>HOLIDAY_ALGORITHM_NAME</code>+N</li>
	 * </ol>
	 */
	public HolidayExpressionCalculatedImpl(final String expression) {
		if (null == expression) {
			throw new IllegalArgumentException("argument 'expression' cannot be null");
		}
		Pattern p = Pattern.compile(REGEX_CALCULATED_EXPRESSION);
		Matcher m = p.matcher(expression.trim());
		if (!m.matches() || 1 < m.groupCount()) {
			throw new IllegalArgumentException("Invalid argument 'expression'");
		}
		String algorithmName = null, operator = null, days = null;
		switch (m.groupCount()) {
		case 4:
			algorithmName = m.group(1);
			operator = m.group(3);
			days = m.group(4);
			break;
		case 1:
		default:
			algorithmName = m.group(1);
			operator = "+";
			days = "0";
			break;
		}
		
		try {
			int daysInt = Integer.parseInt(days);
			daysFromCalculated = daysInt * (("-".equals(operator)) ? -1 : 1);
			algorithm = HolidayAlgorithmLoader.load(algorithmName);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid expression: " + expression, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		Date computed = algorithm.compute(year);
		Calendar c = Calendar.getInstance();
		c.setTime(computed);
		c.add(Calendar.DAY_OF_MONTH, daysFromCalculated);
		return c.getTime();
	}

}
