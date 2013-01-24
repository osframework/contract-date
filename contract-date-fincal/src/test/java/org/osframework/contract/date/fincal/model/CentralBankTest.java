package org.osframework.contract.date.fincal.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

public class CentralBankTest {

	@Test
	public void testEquals() {
		CentralBank cb1 = new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
		CentralBank cb2 = new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
		assertNotSame(cb1, cb2);
		assertEquals(cb1, cb2);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankId() {
		new CentralBank(" ", "United States Federal Reserve", "US", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankName() {
		new CentralBank("USFR", " ", "US", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCountry() {
		new CentralBank("USFR", "United States Federal Reserve", "USA", "USD");
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorInvalidCurrency() {
		new CentralBank("USFR", "United States Federal Reserve", "US", "US");
		fail("Expected IllegalArgument exception to be thrown");
	}

}
