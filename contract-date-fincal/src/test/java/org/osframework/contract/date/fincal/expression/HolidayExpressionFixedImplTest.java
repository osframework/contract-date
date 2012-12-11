package org.osframework.contract.date.fincal.expression;

import static org.osframework.contract.date.testng.Assert.assertSameDate;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HolidayExpressionFixedImplTest {

	@Test(dataProvider = "dp")
	public void testEvaluate(String e, int y, Date check) {
		HolidayExpressionFixedImpl expr = new HolidayExpressionFixedImpl(e);
		Date result = expr.evaluate(y);
		assertSameDate(result, check);
	}

	@DataProvider
	public Object[][] dp() {
		String expression = "DECEMBER/25";
		int year = 2012;
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.DECEMBER, 25);
		Date check = c.getTime();
		Object[] set1 = new Object[] { expression, year, check };
		
		expression = "JANUARY/01";
		year = 2010;
		c.set(2010, Calendar.JANUARY, 1);
		check = c.getTime();
		Object[] set2 = new Object[] { expression, year, check };
		
		expression = "JULY/04";
		year = 2011;
		c.set(2011, Calendar.JULY, 4);
		check = c.getTime();
		Object[] set3 = new Object[] { expression, year, check };
		
		return new Object[][] {
			set1, set2, set3,
		};
	}

}
