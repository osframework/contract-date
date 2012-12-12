package org.osframework.contract.date.testng;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AssertTest {

	@Test(dataProvider = "dates")
	public void testAssertSameDate(Date d1, Date d2) {
		Assert.assertSameDate(d1, d2);
	}

	@Test(dataProvider = "calendars")
	public void testAssertSameDate(Calendar c1, Calendar c2) {
		Assert.assertSameDate(c1, c2);
	}

	@DataProvider
	public Object[][] dates() {
		Calendar c1 = Calendar.getInstance(), c2 = (Calendar)c1.clone();
		c1.set(Calendar.HOUR_OF_DAY, 8);
		c2.set(Calendar.HOUR_OF_DAY, 10);
		Object[] set1 = new Object[] { c1.getTime(), c2.getTime() };
		
		c1.set(Calendar.MINUTE, 0);
		c2.set(Calendar.MINUTE, 15);
		Object[] set2 = new Object[] { c1.getTime(), c2.getTime() };
		
		return new Object[][] {
			set1, set2,
		};
	}

	@DataProvider
	public Object[][] calendars() {
		Calendar c1 = Calendar.getInstance(), c2 = (Calendar)c1.clone();
		c1.set(Calendar.HOUR_OF_DAY, 8);
		c2.set(Calendar.HOUR_OF_DAY, 10);
		Object[] set1 = new Object[] { c1, c2 };
		
		c1.set(Calendar.MINUTE, 0);
		c2.set(Calendar.MINUTE, 15);
		Object[] set2 = new Object[] { c1, c2 };
		
		return new Object[][] {
			set1, set2,
		};
	}

}
