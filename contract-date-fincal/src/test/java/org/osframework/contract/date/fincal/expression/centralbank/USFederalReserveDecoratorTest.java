/*
 * File: USFederalReserveDecoratorTest.java
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
package org.osframework.contract.date.fincal.expression.centralbank;

import static org.osframework.contract.date.fincal.ObjectMother.*;
import static org.osframework.testng.Assert.*;
import static org.testng.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

/**
 * Unit tests for <code>USFederalReserveDecorator</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class USFederalReserveDecoratorTest {

	@Test(groups = "centralbank",
		  dependsOnGroups = "expression",
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullArg() {
		new USFederalReserveDecorator(null);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups = "centralbank",
		  dependsOnGroups = "expression",
		  dataProvider = "dp")
	public void testEvaluate(HolidayExpression expr, int year, Date expected) {
		USFederalReserveDecorator usfrd = new USFederalReserveDecorator(expr);
		Date actual = usfrd.evaluate(year);
		assertSameDay(actual, expected);
	}

	@Test(groups = "centralbank",
		  dependsOnGroups = "expression",
		  dataProvider = "cb")
	public void testSupports(HolidayExpression expr, CentralBank cb, boolean expected) {
		USFederalReserveDecorator usfrd = new USFederalReserveDecorator(expr);
		boolean actual = usfrd.supports(cb);
		assertEquals(actual, expected);
	}

	@DataProvider
	public Object[][] cb() {
		HolidayDefinition nydDef = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
		Object[] set1 = new Object[] { nydDef, createCentralBank(CENTRAL_BANK_ID_BOE), false };
		Object[] set2 = new Object[] { nydDef, createCentralBank(CENTRAL_BANK_ID_USFR), true };
		return new Object[][] {
			set1, set2
		};
	}

	@DataProvider
	public Object[][] dp() {
		HolidayDefinition nydDef = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
		int year = 2012;
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.JANUARY, 2);
		Date check = c.getTime();
		Object[] set1 = new Object[] { nydDef, year, check };
		
		year = 2013;
		c.set(2013, Calendar.JANUARY, 1);
		check = c.getTime();
		Object[] set2 = new Object[] { nydDef, year, check };
		
		return new Object[][] {
			set1, set2
		};
	}
}
