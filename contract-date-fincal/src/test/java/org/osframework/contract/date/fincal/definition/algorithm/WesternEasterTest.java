/*
 * File: WesternEasterTest.java
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
package org.osframework.contract.date.fincal.definition.algorithm;

import static org.osframework.testng.Assert.assertSameDay;
import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>WesternEaster</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class WesternEasterTest {

	@Test(dataProvider = "dp", groups = "algorithm")
	public void testCompute(int year, Date check) {
		WesternEaster we = new WesternEaster();
		Date result = we.compute(year);
		
		// Easter must be a Sunday
		Calendar c = Calendar.getInstance();
		c.setTime(result);
		assertEquals(c.get(Calendar.DAY_OF_WEEK), Calendar.SUNDAY);
		
		assertSameDay(result, check);
	}

	@DataProvider
	public Object[][] dp() {
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.APRIL, 8);
		Object[] set1 = new Object[]{ 2012, c.getTime() };
		
		c.set(2005, Calendar.MARCH, 27);
		Object[] set2 = new Object[]{ 2005, c.getTime() };
		
		c.set(1999, Calendar.APRIL, 4);
		Object[] set3 = new Object[]{ 1999, c.getTime() };
		
		return new Object[][] {
			set1, set2, set3,
		};
	}

}
