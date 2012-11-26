package org.osframework.contract.date.holiday.expr;

import static org.testng.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class HolidayExpressionRelativeImplTest {

	@Test(dataProvider = "dp")
	public void testEvaluate(String e, int y, Date check) {
		HolidayExpressionRelativeImpl expr = new HolidayExpressionRelativeImpl(e);
		Date result = expr.evaluate(y);
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
		String expression = "NOVEMBER/THURSDAY/4";
		int year = 2012;
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.NOVEMBER, 22);
		Date check = c.getTime();
		Object[] set1 = new Object[] { expression, year, check };
		
		expression = "SEPTEMBER/MONDAY/1";
		year = 2010;
		c.set(2010, Calendar.SEPTEMBER, 6);
		check = c.getTime();
		Object[] set2 = new Object[] { expression, year, check };
		
		return new Object[][] {
			set1, set2, 
		};
	}

}
