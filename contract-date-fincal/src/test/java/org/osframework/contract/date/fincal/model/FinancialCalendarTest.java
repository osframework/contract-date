/*
 * File: FinancialCalendarTest.java
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
package org.osframework.contract.date.fincal.model;

import static org.osframework.contract.date.fincal.ObjectMother.createCentralBank;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinitions;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Currency;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.Test;

/**
 * Unit tests for <code>FinancialCalendar</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FinancialCalendarTest {

	@Test(groups = {"model"})
	public void testEquals() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc1 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		FinancialCalendar fc2 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		assertNotSame(fc1, fc2);
		assertEquals(fc1, fc2);
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankId() {
		new FinancialCalendar(" ", "New York bank holidays", createCentralBank(), createHolidayDefinitions());
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankDescription() {
		new FinancialCalendar("NYB", " ", createCentralBank(), createHolidayDefinitions());
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullCentralBank() {
		new FinancialCalendar("NYB", "New York bank holidays", null, createHolidayDefinitions());
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullHolidayDefinitions() {
		new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), null);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"})
	public void testSize() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		assertEquals(fc.size(), hds.size());
	}

	@Test(groups = {"model"})
	public void testContains() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		for (HolidayDefinition hd : hds) {
			assertTrue(fc.contains(hd));
		}
	}

	@Test(groups = {"model"})
	public void testIterator() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		for (HolidayDefinition hd : fc) {
			assertTrue(hds.contains(hd));
		}
		try {
			Iterator<HolidayDefinition> it = fc.iterator();
			it.next();
			it.remove();
			fail("Expected UnsupportedOperationException exception to be thrown");
		} catch (UnsupportedOperationException uoe) {
			
		}
	}

	@Test(groups = {"model"})
	public void testGetCurrency() {
		CentralBank cb = createCentralBank();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, createHolidayDefinitions());
		Currency c1 = cb.getCurrency();
		Currency c2 = fc.getCurrency();
		assertEquals(c2, c1);
		assertSame(c2, c1);
	}

}
