package org.osframework.contract.date.fincal.data.file.csv;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.osframework.contract.date.fincal.data.file.AbstractDelimitedTextFileOutputTest;
import org.osframework.contract.date.fincal.model.Holiday;
import org.testng.annotations.Test;

public class TriColCsvOutputTest extends AbstractDelimitedTextFileOutputTest {

	@Test(dataProvider = "oneHoliday")
	public void testStoreOneHoliday(Holiday holiday, String expected) throws IOException {
		TriColCsvOutput tcco = new TriColCsvOutput(out);
		tcco.store(holiday);
		tcco.close();
		String actual = out.toString();
		assertEquals(actual, expected);
	}

	@Test(dataProvider = "twoHolidays")
	public void testStoreHolidayArray(Holiday[] holidays, String[] expecteds) throws IOException {
		TriColCsvOutput tcco = new TriColCsvOutput(out);
		tcco.store(holidays);
		tcco.close();
		String actual = out.toString();
		String[] actuals = actual.split(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < expecteds.length; i++) {
			assertEquals(actuals[i], expecteds[i]);
		}
	}

	@Override
	protected String holidayToExpectedString(Holiday h) {
		StringBuilder csv = new StringBuilder()
                                .append(h.getDate())
                                .append(",")
                                .append(h.getFinancialCalendar().getId())
                                .append(",")
                                .append(h.getHolidayDefinition().getName());
		return csv.toString();
	}

}
