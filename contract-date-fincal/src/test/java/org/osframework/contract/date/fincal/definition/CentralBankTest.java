/*
 * File: CentralBankTest.java
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

import static org.osframework.contract.date.fincal.ObjectMother.CENTRAL_BANK_ID_USFR;
import static org.osframework.contract.date.fincal.ObjectMother.createCentralBank;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Currency;

import org.testng.annotations.Test;

/**
 * Unit tests for <code>CentralBank</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CentralBankTest {

	@Test(groups = {"definition"})
	public void testEquals() {
		CentralBank cb1 = createCentralBank(CENTRAL_BANK_ID_USFR);
		CentralBank cb2 = createCentralBank(CENTRAL_BANK_ID_USFR);
		assertNotSame(cb1, cb2);
		assertEquals(cb1, cb2);
	}

	@Test(groups = {"definition"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCountry() {
		new CentralBank("USFR", "United States Federal Reserve", "USA", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"definition"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCurrency() {
		new CentralBank("USFR", "United States Federal Reserve", "US", "US");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"definition"})
	public void testToImmutable() {
		CentralBank mutable = createCentralBank(CENTRAL_BANK_ID_USFR);
		CentralBank immutable = mutable.toImmutable();
		
		// Test equality but non-identity
		assertEquals(immutable, mutable);
		assertNotSame(immutable, mutable);
		
		// Test that changes to mutable object do not appear in immutable object
		assertTrue(immutable.getId().equals(mutable.getId()));
		mutable.setId("USFS");
		assertFalse(immutable.getId().equals(mutable.getId()));
		
		assertTrue(immutable.getName().equals(mutable.getName()));
		mutable.setName("Some other name");
		assertFalse(immutable.getName().equals(mutable.getName()));
		
		assertTrue(immutable.getCountry().equals(mutable.getCountry()));
		mutable.setCountry("UK");
		assertFalse(immutable.getCountry().equals(mutable.getCountry()));
		
		assertTrue(immutable.getCurrency().equals(mutable.getCurrency()));
		mutable.setCurrency("GBP");
		assertFalse(immutable.getCurrency().equals(mutable.getCurrency()));
		
		// Test that state cannot get changed via setters on immutable object
		immutable = createCentralBank(CENTRAL_BANK_ID_USFR).toImmutable();
		try {
			immutable.setId("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setName("Other");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setCountry("UK");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setCurrency("GBP");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
		try {
			immutable.setCurrency(Currency.getInstance("GBP"));
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {}
	}

	@Test(groups = {"definition"})
	public void testIsImmutable() {
		CentralBank mutable = createCentralBank(CENTRAL_BANK_ID_USFR);
		CentralBank immutable = mutable.toImmutable();
		
		assertFalse(mutable.isImmutable());
		assertTrue(immutable.isImmutable());
	}

}
