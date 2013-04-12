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
package org.osframework.contract.date.fincal.definition;

import static org.osframework.contract.date.fincal.ObjectMother.CENTRAL_BANK_ID_BOE;
import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_NEW_YEARS_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.createCentralBank;
import static org.osframework.contract.date.fincal.ObjectMother.createFinancialCalendar;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinition;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinitions;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Collections;
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

	@Test(groups = {"definition"})
	public void testEquals() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc1 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		FinancialCalendar fc2 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		assertNotSame(fc1, fc2);
		assertEquals(fc1, fc2);
	}

	@Test(groups = {"definition"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullCentralBank() {
		new FinancialCalendar("NYB", "New York bank holidays", null, createHolidayDefinitions());
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"definition"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullHolidayDefinitions() {
		new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), null);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"definition"})
	public void testSize() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		assertEquals(fc.size(), hds.size());
	}

	@Test(groups = {"definition"})
	public void testContains() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		for (HolidayDefinition hd : hds) {
			assertTrue(fc.contains(hd));
		}
	}

	@Test(groups = {"definition"})
	public void testIterator() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", createCentralBank(), hds);
		for (HolidayDefinition hd : fc) {
			assertTrue(hds.contains(hd));
		}
		
		// Test that collection can be updated through iterator
		Iterator<HolidayDefinition> it = fc.iterator();
		it.next();
		it.remove();
	}

	@Test(groups = {"definition"})
	public void testGetCurrency() {
		CentralBank cb = createCentralBank();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, createHolidayDefinitions());
		Currency c1 = cb.getCurrency();
		Currency c2 = fc.getCurrency();
		assertEquals(c2, c1);
		assertSame(c2, c1);
	}

	@Test(groups = {"definition"})
	public void testToImmutable() {
		FinancialCalendar mutable = createFinancialCalendar();
		FinancialCalendar immutable = mutable.toImmutable();
		
		// Test equality but non-identity
		assertEquals(immutable, mutable);
		assertNotSame(immutable, mutable);
		
		// Test that changes to mutable object do not appear in immutable object
		assertTrue(immutable.getId().equals(mutable.getId()));
		mutable.setId("XYZ");
		assertFalse(immutable.getId().equals(mutable.getId()));
		
		assertTrue(immutable.getDescription().equals(mutable.getDescription()));
		mutable.setDescription("XYZ bank holidays");
		assertFalse(immutable.getDescription().equals(mutable.getDescription()));
		
		assertTrue(immutable.getHolidayDefinitions().equals(mutable.getHolidayDefinitions()));
		Set<HolidayDefinition> emptySet = Collections.emptySet();
		mutable.setHolidayDefinitions(emptySet);
		assertFalse(immutable.getHolidayDefinitions().equals(mutable.getHolidayDefinitions()));
		
		assertTrue(immutable.getCentralBank().equals(mutable.getCentralBank()));
		mutable.setCentralBank(createCentralBank(CENTRAL_BANK_ID_BOE));
		assertFalse(immutable.getCentralBank().equals(mutable.getCentralBank()));
		
		// Test that state cannot get changed via setters, iterator on immutable object
		immutable = createFinancialCalendar().toImmutable();
		try {
			immutable.setId("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setDescription("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			Set<HolidayDefinition> hds = Collections.emptySet();
			immutable.setHolidayDefinitions(hds);
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.getHolidayDefinitions().clear();
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			HolidayDefinition hd = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
			immutable.addHolidayDefinition(hd);
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			HolidayDefinition hd = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
			immutable.removeHolidayDefinition(hd);
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			Iterator<HolidayDefinition> it = immutable.iterator();
			it.next();
			it.remove();
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
	}

	@Test(groups = {"definition"})
	public void testIsImmutable() {
		FinancialCalendar mutable = createFinancialCalendar();
		FinancialCalendar immutable = mutable.toImmutable();
		
		assertFalse(mutable.isImmutable());
		assertTrue(immutable.isImmutable());
	}

}
