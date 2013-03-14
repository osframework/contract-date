/*
 * File: HolidayTest.java
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
package org.osframework.contract.date.fincal.holiday;

import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_MLK_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.createFinancialCalendar;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinition;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>Holiday</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayTest {

	@Test(groups = {"model"})
	public void testEquals() {
		FinancialCalendar fc = createFinancialCalendar();
		HolidayDefinition mlkDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		Holiday h1 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		Holiday h2 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		assertEquals(h2, h1);
		assertNotSame(h2, h1);
		
		h1 = new Holiday(fc, 20120121, mlkDef);
		h2 = new Holiday(fc, 20120121, mlkDef);
		assertEquals(h2, h1);
		assertNotSame(h2, h1);
	}

	@Test(groups = {"model"})
	public void testGetKey() {
		FinancialCalendar fc = createFinancialCalendar();
		HolidayDefinition mlkDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		Holiday h1 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef),
				h2 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		HolidayId k1 = h1.getId(),
				  k2 = h2.getId();
		assertEquals(k2, k1);
		assertNotSame(k2, k1);
	}

	@Test(groups = {"model"})
	public void testCompareTo() {
		FinancialCalendar fc = createFinancialCalendar();
		HolidayDefinition mlkDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		Holiday h1 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef),
				h2 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		int c = h1.compareTo(h2);
		assertEquals(c, 0);
		c = h2.compareTo(h1);
		assertEquals(c, 0);
		
		h2 = new Holiday(fc, mlkDef.evaluate(2011), mlkDef);
		c = h1.compareTo(h2);
		assertTrue(c > 0);
		c = h2.compareTo(h1);
		assertTrue(c < 0);
		
		h2 = new Holiday(fc, mlkDef.evaluate(2013), mlkDef);
		c = h1.compareTo(h2);
		assertTrue(c < 0);
		c = h2.compareTo(h1);
		assertTrue(c > 0);
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullFinancialCalendar() {
		HolidayDefinition mlkDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		new Holiday(null, mlkDef.evaluate(2012), mlkDef);
	}

/*
 * FIXME Found incorrect behavior in DateUtil.formateDateToInt due to Joda-Time class DateTime
 * 
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullDate() {
		FinancialCalendar fc = getNYBFinancialCalendar();
		HolidayDefinition mlkDef = getMLKDayDefinition();
		new Holiday(fc, null, mlkDef);
	}
 *
 *
 */

}
