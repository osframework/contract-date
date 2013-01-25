/*
 * File: HolidayExpressionCalculatedImplTest.java
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
package org.osframework.contract.date.fincal.expression;

import static org.osframework.testng.Assert.assertSameDay;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>HolidayExpressionCalculatedImpl</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayExpressionCalculatedImplTest {

	@Test(groups = "expression",
		  dependsOnGroups = "algorithm",
		  dataProvider = "dp")
	public void testEvaluate(String e, int y, Date check) {
		HolidayExpressionCalculatedImpl expr = new HolidayExpressionCalculatedImpl(e);
		Date result = expr.evaluate(y);
		assertSameDay(result, check);
	}

	@DataProvider
	public Object[][] dp() {
		String expression = "EASTER-2";
		int year = 2012;
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.APRIL, 6);
		Date check = c.getTime();
		Object[] set1 = new Object[] { expression, year, check };
		
		year = 2010;
		c.set(2010, Calendar.APRIL, 2);
		check = c.getTime();
		Object[] set2 = new Object[] { expression, year, check };
		
		expression = "EASTER+50";
		year = 2015;
		c.set(2015, Calendar.MAY, 25);
		check = c.getTime();
		Object[] set3 = new Object[] { expression, year, check };
		
		expression = "EASTER";
		year = 1977;
		c.set(1977, Calendar.APRIL, 10);
		check = c.getTime();
		Object[] set4 = new Object[] { expression, year, check };
		
		return new Object[][] {
			set1, set2, set3, set4
		};
	}

}
