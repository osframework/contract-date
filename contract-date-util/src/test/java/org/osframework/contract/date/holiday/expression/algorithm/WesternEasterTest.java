package org.osframework.contract.date.holiday.expression.algorithm;

import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class WesternEasterTest {

	@Test(dataProvider = "dp")
	public void testCompute(int year, Date check) {
		WesternEaster we = new WesternEaster();
		Date result = we.compute(year);
		Calendar actual = Calendar.getInstance(),
				 expected = (Calendar)actual.clone();
		actual.setTime(result);
		expected.setTime(check);
		assertEquals(actual.get(Calendar.YEAR), expected.get(Calendar.YEAR));
		assertEquals(actual.get(Calendar.MONTH), expected.get(Calendar.MONTH));
		assertEquals(actual.get(Calendar.DAY_OF_MONTH), expected.get(Calendar.DAY_OF_MONTH));
	}

	@DataProvider
	public Object[][] dp() {
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.APRIL, 8);
		Object[] set1 = new Object[]{ 2012, c.getTime() };
		
		c.set(2005, Calendar.MARCH, 27);
		Object[] set2 = new Object[]{ 2005, c.getTime() };
		
		c.set(1999, Calendar.APRIL, 4);
		Object[] set3 = new Object[]{ 1999, c.getTime() };
		
		return new Object[][] {
			set1, set2, set3,
		};
	}

}
