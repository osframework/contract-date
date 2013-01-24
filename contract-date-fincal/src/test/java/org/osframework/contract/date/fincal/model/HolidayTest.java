package org.osframework.contract.date.fincal.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import java.util.Collections;

import org.testng.annotations.Test;

public class HolidayTest {

	@Test
	public void testEquals() {
		FinancialCalendar fc = getNYBFinancialCalendar();
		HolidayDefinition mlkDef = getMLKDayDefinition();
		Holiday h1 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		Holiday h2 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		assertEquals(h2, h1);
		assertNotSame(h2, h1);
		
		h1 = new Holiday(fc, 20120121, mlkDef);
		h2 = new Holiday(fc, 20120121, mlkDef);
		assertEquals(h2, h1);
		assertNotSame(h2, h1);
	}

	@Test
	public void testGetKey() {
		FinancialCalendar fc = getNYBFinancialCalendar();
		HolidayDefinition mlkDef = getMLKDayDefinition();
		Holiday h1 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef),
				h2 = new Holiday(fc, mlkDef.evaluate(2012), mlkDef);
		HolidayKey k1 = h1.getKey(),
				   k2 = h2.getKey();
		assertEquals(k2, k1);
		assertNotSame(k2, k1);
	}

	@Test
	public void testCompareTo() {
		FinancialCalendar fc = getNYBFinancialCalendar();
		HolidayDefinition mlkDef = getMLKDayDefinition();
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

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullFinancialCalendar() {
		HolidayDefinition mlkDef = getMLKDayDefinition();
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

	private HolidayDefinition getMLKDayDefinition() {
		return new HolidayDefinition("MLKDay",
				                     "Martin Luther King Day",
				                     "Birthday of Martin Luther King, Jr.",
				                     HolidayType.RELATIVE,
				                     "JANUARY/MONDAY/3");
	}

	private FinancialCalendar getNYBFinancialCalendar() {
		return new FinancialCalendar("NYB",
				                     "New York bank holidays",
				                     getCentralBank(),
				                     Collections.singleton(getMLKDayDefinition()));
	}

	private CentralBank getCentralBank() {
		return new CentralBank("USFR", "United States Federal Reserve", "US", "USD");
	}

}
