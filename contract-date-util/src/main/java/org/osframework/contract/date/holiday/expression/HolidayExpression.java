/**
 * File: HolidayExpression.java
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
package org.osframework.contract.date.holiday.expression;

import java.util.Date;

/**
 * Behavior exposed by objects that produce the date of a holiday through
 * evaluation of some logic.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public interface HolidayExpression {

	/**
	 * Produce the date of a defined holiday for the specified year.
	 * 
	 * @param year year for which this expression must produce the holiday date
	 * @return date of a defined holiday in the given year
	 */
	public Date evaluate(int year);

}