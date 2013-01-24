/*
 * File: HolidayExpressionRelativeImplTest.java
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
 * Unit tests for <code>HolidayExpressionRelativeImpl</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayExpressionRelativeImplTest {

	@Test(dataProvider = "dp", groups = "expression")
	public void testEvaluate(String e, int y, Date check) {
		HolidayExpressionRelativeImpl expr = new HolidayExpressionRelativeImpl(e);
		Date result = expr.evaluate(y);
		assertSameDay(result, check);
	}

	@DataProvider
	public Object[][] dp() {
		String expression = "NOVEMBER/THURSDAY/4";
		int year = 2012;
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.NOVEMBER, 22);
		Date check = c.getTime();
		Object[] set1 = new Object[] { expression, year, check };
		
		expression = "SEPTEMBER/MONDAY/1";
		year = 2010;
		c.set(2010, Calendar.SEPTEMBER, 6);
		check = c.getTime();
		Object[] set2 = new Object[] { expression, year, check };
		
		return new Object[][] {
			set1, set2, 
		};
	}

}
