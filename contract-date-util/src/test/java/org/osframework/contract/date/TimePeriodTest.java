package org.osframework.contract.date;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TimePeriodTest {

	@Test(dataProvider = "validNotations")
	public void testConstructorWithStringArgValid(String notation, int checkAmount, TimeUnit checkTimeUnit) {
		TimePeriod tp = new TimePeriod(notation);
		assertEquals(tp.getAmount(), checkAmount);
		assertEquals(tp.getTimeUnit(), checkTimeUnit);
	}

	@DataProvider
	public Object[][] validNotations() {
		return new Object[][] {
			new Object[] { "0D", 0, TimeUnit.DAY },
			new Object[] { "2B", 2, TimeUnit.BUSINESS_DAY },
			new Object[] { "3M", 3, TimeUnit.MONTH },
			new Object[] { "10Y", 10, TimeUnit.YEAR },
			new Object[] { "-150Y", -150, TimeUnit.YEAR }
		};
	}

}
