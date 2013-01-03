package org.osframework.contract.date.fincal.expression;

import static org.osframework.testng.Assert.assertSameDay;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HolidayExpressionRelativeImplTest {

	@Test(dataProvider = "dp", groups = "expression")
	public void testEvaluate(String e, int y, Date check) {
		HolidayExpressionRelativeImpl expr = new HolidayExpressionRelativeImpl(e);
		Date result = expr.evaluate(y);
		assertSameDay(result, check);
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
