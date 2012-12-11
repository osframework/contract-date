package org.osframework.contract.date.fincal.expression.algorithm;

import static org.osframework.contract.date.testng.Assert.assertSameDate;
import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WesternEasterTest {

	@Test(dataProvider = "dp", groups = "algorithm")
	public void testCompute(int year, Date check) {
		WesternEaster we = new WesternEaster();
		Date result = we.compute(year);
		
		// Easter must be a Sunday
		Calendar c = Calendar.getInstance();
		c.setTime(result);
		assertEquals(c.get(Calendar.DAY_OF_WEEK), Calendar.SUNDAY);
		
		assertSameDate(result, check);
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
