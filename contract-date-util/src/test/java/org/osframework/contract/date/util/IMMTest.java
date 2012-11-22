package org.osframework.contract.date.util;

import static org.testng.Assert.*;
import static org.testng.ContractDateAssert.*;

import java.util.Calendar;
import java.util.Date;

import org.osframework.contract.date.util.IMM;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class IMMTest {

	@Test(dataProvider = "immCodes")
	public void testIsIMMCode1Arg(String immCode, boolean check) {
		boolean result = IMM.isIMMCode(immCode);
		assertEquals(result, check);
	}

	@Test(dataProvider = "immCodesWithFlags")
	public void testIsIMMCode2Args(String immCode, boolean mainCycle, boolean check) {
		boolean result = IMM.isIMMCode(immCode, mainCycle);
		assertEquals(result, check);
	}

	@Test(dataProvider = "immDates")
	public void testIsIMMDate1Arg(Date immDate, boolean check) {
		boolean result = IMM.isIMMDate(immDate);
		assertEquals(result, check);
	}

	@Test(dataProvider = "immCodesAndReferenceDates")
	public void testToDate2Args(String immCode, Date refDate, Date checkDate) {
		Date result = IMM.toDate(immCode, refDate);
		assertSameDay(result, checkDate, "Conversion did not produce correct IMM date");
	}

	@DataProvider
	public Object[][] immCodes() {
		return new Object[][] {
			new Object[] { "H3", true },
			new Object[] { "Z9", true },
			new Object[] { "X0", true },
			new Object[] { "W1", false },
		};
	}

	@DataProvider
	public Object[][] immCodesWithFlags() {
		return new Object[][] {
			new Object[] { "H3", true, true },
			new Object[] { "Z9", true, true },
			new Object[] { "X0", true, false },
			new Object[] { "W1", false, false },
			new Object[] { "F1", false, true },
		};
	}

	@DataProvider
	public Object[][] immDates() {
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.DECEMBER, 19);
		Object[] d1 = new Object[] { c.getTime(), true };
		
		c.set(2019, Calendar.DECEMBER, 18);
		Object[] d2 = new Object[] { c.getTime(), true };
		
		c.set(2032, Calendar.NOVEMBER, 17);
		Object[] d3 = new Object[] { c.getTime(), true };
		
		return new Object[][] {
			d1, d2, d3
		};
	}

	@DataProvider
	public Object[][] immCodesAndReferenceDates() {
		Calendar r = Calendar.getInstance(),
				 c = (Calendar)r.clone();
		r.set(2012, Calendar.NOVEMBER, 20);
		
		c.set(2019, Calendar.DECEMBER, 18);
		Object[] d1 = new Object[] { "Z9", r.getTime(), c.getTime() };
		
		return new Object[][] {
			d1
		};
	}

}
