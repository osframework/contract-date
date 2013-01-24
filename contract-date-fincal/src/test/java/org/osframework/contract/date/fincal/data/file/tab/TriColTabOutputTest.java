package org.osframework.contract.date.fincal.data.file.tab;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.osframework.contract.date.fincal.data.file.AbstractDelimitedTextFileOutputTest;
import org.osframework.contract.date.fincal.model.Holiday;
import org.testng.annotations.Test;

public class TriColTabOutputTest extends AbstractDelimitedTextFileOutputTest {

	@Test(dataProvider = "oneHoliday")
	public void testStoreOneHoliday(Holiday holiday, String expected) throws IOException {
		TriColTabOutput tcto = new TriColTabOutput(out);
		tcto.store(holiday);
		tcto.close();
		String actual = out.toString();
		assertEquals(actual, expected);
	}

	@Test(dataProvider = "twoHolidays")
	public void testStoreHolidayArray(Holiday[] holidays, String[] expecteds) throws IOException {
		TriColTabOutput tcto = new TriColTabOutput(out);
		tcto.store(holidays);
		tcto.close();
		String actual = out.toString();
		String[] actuals = actual.split(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < expecteds.length; i++) {
			assertEquals(actuals[i], expecteds[i]);
		}
	}

	@Override
	protected String holidayToExpectedString(Holiday h) {
		StringBuilder tsv = new StringBuilder()
                                .append(h.getDate())
                                .append('\t')
                                .append(h.getFinancialCalendar().getId())
                                .append('\t')
                                .append(h.getHolidayDefinition().getName());
		return tsv.toString();
	}

}
