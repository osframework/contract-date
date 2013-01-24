package org.osframework.contract.date.fincal.model;

import static org.testng.Assert.*;

import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.Test;

public class FinancialCalendarTest {

	@Test
	public void testEquals() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc1 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		FinancialCalendar fc2 = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		assertNotSame(fc1, fc2);
		assertEquals(fc1, fc2);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankId() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		new FinancialCalendar(" ", "New York bank holidays", cb, hds);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankDescription() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		new FinancialCalendar("NYB", " ", cb, hds);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullCentralBank() {
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		new FinancialCalendar("NYB", "New York bank holidays", null, hds);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullHolidayDefinitions() {
		CentralBank cb = createCentralBank();
		new FinancialCalendar("NYB", "New York bank holidays", cb, null);
		fail("Expected IllegalArgument exception to be thrown");
	}

	@Test
	public void testSize() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		assertEquals(fc.size(), hds.size());
	}

	@Test
	public void testContains() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		for (HolidayDefinition hd : hds) {
			assertTrue(fc.contains(hd));
		}
	}

	@Test
	public void testIterator() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
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

	@Test
	public void testGetCurrency() {
		CentralBank cb = createCentralBank();
		Set<HolidayDefinition> hds = createHolidayDefinitions();
		FinancialCalendar fc = new FinancialCalendar("NYB", "New York bank holidays", cb, hds);
		Currency c1 = cb.getCurrency();
		Currency c2 = fc.getCurrency();
		assertEquals(c2, c1);
		assertSame(c2, c1);
	}

	private CentralBank createCentralBank() {
		return new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
	}

	private Set<HolidayDefinition> createHolidayDefinitions() {
		Set<HolidayDefinition> hds = new HashSet<HolidayDefinition>();
		hds.add(new HolidayDefinition("MLKDay",
                                      "Martin Luther King Day",
                                      "Birthday of Martin Luther King, Jr.",
                                      HolidayType.RELATIVE,
                                      "JANUARY/MONDAY/3"));
		hds.add(new HolidayDefinition("IndependenceDay",
				                      "Independence Day",
				                      "Celebration of 1776 Declaration of Independence",
				                      HolidayType.FIXED,
				                      "JULY/01"));
		return hds;
	}

}
