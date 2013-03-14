/*
 * File: TriColTabOutputTest.java
 * 
 * Copyright 2013 OSFramework Project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osframework.contract.date.fincal.output.file.tab;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.osframework.contract.date.fincal.output.file.AbstractDelimitedTextFileOutputTest;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>TriColTabOutput</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class TriColTabOutputTest extends AbstractDelimitedTextFileOutputTest {

	@Test(groups="data",
		  dependsOnGroups="model",
		  dataProvider = "oneHoliday")
	public void testStoreOneHoliday(Holiday holiday, String expected) throws IOException {
		TriColTabOutput tcto = new TriColTabOutput(file);
		tcto.store(holiday);
		tcto.close();
		String actual = actualFileContent(file);
		assertEquals(actual, expected);
	}

	@Test(groups="data",
		  dependsOnGroups="model",
		  dataProvider = "twoHolidays")
	public void testStoreHolidayArray(Holiday[] holidays, String[] expecteds) throws IOException {
		TriColTabOutput tcto = new TriColTabOutput(file);
		tcto.store(holidays);
		tcto.close();
		String actual = actualFileContent(file);
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
