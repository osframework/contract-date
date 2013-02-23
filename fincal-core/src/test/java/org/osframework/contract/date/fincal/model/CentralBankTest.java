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
package org.osframework.contract.date.fincal.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * Unit tests for <code>CentralBank</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CentralBankTest {

	@Test(groups = {"model"})
	public void testEquals() {
		CentralBank cb1 = new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
		CentralBank cb2 = new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
		assertNotSame(cb1, cb2);
		assertEquals(cb1, cb2);
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankId() {
		new CentralBank(" ", "United States Federal Reserve", "US", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankName() {
		new CentralBank("USFR", " ", "US", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCountry() {
		new CentralBank("USFR", "United States Federal Reserve", "USA", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(groups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCurrency() {
		new CentralBank("USFR", "United States Federal Reserve", "US", "US");
		fail("Expected IllegalArgument exception to be thrown");
	}

}
