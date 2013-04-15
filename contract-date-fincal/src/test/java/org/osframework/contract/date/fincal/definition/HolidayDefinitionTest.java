/*
 * File: HolidayDefinitionTest.java
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
package org.osframework.contract.date.fincal.definition;

import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_MLK_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_NEW_YEARS_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_THANKSGIVING;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinition;
import static org.osframework.testng.Assert.assertSameDay;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>HolidayDefinition</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayDefinitionTest {

	@Test(groups = {"definition"})
	public void testEquals() {
		HolidayDefinition hd1 = createHolidayDefinition(HOLIDAY_DEF_ID_THANKSGIVING);
		HolidayDefinition hd2 = createHolidayDefinition(HOLIDAY_DEF_ID_THANKSGIVING);
		assertNotSame(hd2, hd1);
		assertEquals(hd2, hd1);
	}

	@Test(groups = {"definition"}, dataProvider="evaluateData")
	public void testEvaluate(String holidayDefId, int year, Date expected) {
		HolidayDefinition hd = createHolidayDefinition(holidayDefId);
		assertNotNull(hd);
		
		Date actual = hd.evaluate(year);
		assertSameDay(actual, expected);
	}

	@Test(groups = {"definition"}, expectedExceptions={IllegalStateException.class})
	public void testEvaluateNullObservance() {
		HolidayDefinition hd = createHolidayDefinition(HOLIDAY_DEF_ID_THANKSGIVING);
		hd.setObservance(null);
		
		hd.evaluate(2013);
		fail("Expected IllegalStateException to be thrown");
	}

	@Test(groups = {"definition"}, expectedExceptions={IllegalStateException.class})
	public void testEvaluateNullExpression() {
		HolidayDefinition hd = createHolidayDefinition(HOLIDAY_DEF_ID_THANKSGIVING);
		hd.setExpression(null);
		
		hd.evaluate(2013);
		fail("Expected IllegalStateException to be thrown");
	}

	@Test(groups = {"definition"})
	public void testToImmutable() {
		HolidayDefinition mutable = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
		HolidayDefinition immutable = mutable.toImmutable();
		
		// Test equality but non-identity
		assertEquals(immutable, mutable);
		assertNotSame(immutable, mutable);
		
		// Test that changes to mutable object do not appear in immutable object
		assertTrue(immutable.getId().equals(mutable.getId()));
		mutable.setId(HOLIDAY_DEF_ID_MLK_DAY);
		assertFalse(immutable.getId().equals(mutable.getId()));
		
		assertTrue(immutable.getName().equals(mutable.getName()));
		mutable.setName("Some other name");
		assertFalse(immutable.getName().equals(mutable.getName()));
		
		assertTrue(immutable.getDescription().equals(mutable.getDescription()));
		mutable.setDescription("Some other description");
		assertFalse(immutable.getDescription().equals(mutable.getDescription()));
		
		assertTrue(immutable.getObservance().equals(mutable.getObservance()));
		mutable.setObservance(HolidayType.ONETIME);
		assertFalse(immutable.getObservance().equals(mutable.getObservance()));
		
		assertTrue(immutable.getExpression().equals(mutable.getExpression()));
		mutable.setExpression("2012-06-05");
		assertFalse(immutable.getExpression().equals(mutable.getExpression()));
		
		// Test that state cannot get changed via setters on immutable object
		immutable = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY).toImmutable();
		try {
			immutable.setId("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setName("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setDescription("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setObservance(HolidayType.ONETIME);
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setExpression("2012-06-05");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
	}

	@Test(groups = {"definition"})
	public void testIsImmutable() {
		HolidayDefinition mutable = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
		HolidayDefinition immutable = mutable.toImmutable();
		
		assertFalse(mutable.isImmutable());
		assertTrue(immutable.isImmutable());
	}

	@DataProvider
	private Object[][] evaluateData() {
		Calendar c = Calendar.getInstance();
		
		c.set(2013, Calendar.NOVEMBER, 28);
		Object[] d1 = new Object[] { HOLIDAY_DEF_ID_THANKSGIVING, 2013, c.getTime() };
		
		c.set(2012, Calendar.NOVEMBER, 22);
		Object[] d2 = new Object[] { HOLIDAY_DEF_ID_THANKSGIVING, 2012, c.getTime() };
		
		return new Object[][] {
				d1, d2
		};
	}

}
