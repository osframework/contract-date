/*
 * File: AbstractDelimitedTextFileOutputTest.java
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
package org.osframework.contract.date.fincal.output.io;

import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_INDEPENDENCE_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_MLK_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.createFinancialCalendar;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

/**
 * Abstract superclass of concrete delimited textfile output unit tests.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractDelimitedTextFileOutputTest {

	protected File file;

	@BeforeMethod
	public void createOutputStream() throws IOException {
		this.file = File.createTempFile("output-test", null);
	}

	@AfterMethod
	public void destroyOutputStream() throws IOException {
		this.file.delete();
		this.file = null;
	}

	@DataProvider
	public Object[][] oneHoliday() {
		FinancialCalendar fc = createFinancialCalendar();
		HolidayDefinition mlkDayDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		
		Holiday h1 = new Holiday(fc, mlkDayDef.evaluate(2012), mlkDayDef);
		String s1 = holidayToExpectedString(h1);
		Object[] set1 = new Object[] { h1, s1 };
		
		Holiday h2 = new Holiday(fc, mlkDayDef.evaluate(2013), mlkDayDef);
		String s2 = holidayToExpectedString(h2);
		Object[] set2 = new Object[] { h2, s2 };
		
		Holiday h3 = new Holiday(fc, mlkDayDef.evaluate(2014), mlkDayDef);
		String s3 = holidayToExpectedString(h3);
		Object[] set3 = new Object[] { h3, s3 };
		
		Holiday h4 = new Holiday(fc, mlkDayDef.evaluate(2015), mlkDayDef);
		String s4 = holidayToExpectedString(h4);
		Object[] set4 = new Object[] { h4, s4 };
		
		return new Object[][] {
			set1, set2, set3, set4
		};
	}

	@DataProvider
	public Object[][] twoHolidays() {
		FinancialCalendar fc = createFinancialCalendar();
		HolidayDefinition mlkDayDef = createHolidayDefinition(HOLIDAY_DEF_ID_MLK_DAY);
		HolidayDefinition fourthOfJulyDef = createHolidayDefinition(HOLIDAY_DEF_ID_INDEPENDENCE_DAY);
		
		Holiday h1a = new Holiday(fc, mlkDayDef.evaluate(2012), mlkDayDef);
		Holiday h1b = new Holiday(fc, fourthOfJulyDef.evaluate(2012), fourthOfJulyDef);
		String s1a = holidayToExpectedString(h1a);
		String s1b = holidayToExpectedString(h1b);
		Object[] set1 = new Object[] {
			new Holiday[] { h1a, h1b },
			new String[] { s1a, s1b }
		};
		
		Holiday h2a = new Holiday(fc, mlkDayDef.evaluate(2013), mlkDayDef);
		Holiday h2b = new Holiday(fc, fourthOfJulyDef.evaluate(2013), fourthOfJulyDef);
		String s2a = holidayToExpectedString(h2a);
		String s2b = holidayToExpectedString(h2b);
		Object[] set2 = new Object[] {
			new Holiday[] { h2a, h2b },
			new String[] { s2a, s2b }
		};
		
		Holiday h3a = new Holiday(fc, mlkDayDef.evaluate(2014), mlkDayDef);
		Holiday h3b = new Holiday(fc, fourthOfJulyDef.evaluate(2014), fourthOfJulyDef);
		String s3a = holidayToExpectedString(h3a);
		String s3b = holidayToExpectedString(h3b);
		Object[] set3 = new Object[] {
			new Holiday[] { h3a, h3b },
			new String[] { s3a, s3b }
		};
		
		Holiday h4a = new Holiday(fc, mlkDayDef.evaluate(2015), mlkDayDef);
		Holiday h4b = new Holiday(fc, fourthOfJulyDef.evaluate(2015), fourthOfJulyDef);
		String s4a = holidayToExpectedString(h4a);
		String s4b = holidayToExpectedString(h4b);
		Object[] set4 = new Object[] {
			new Holiday[] { h4a, h4b },
			new String[] { s4a, s4b }
		};
		
		return new Object[][] {
			set1, set2, set3, set4
		};
	}

	protected String actualFileContent(File file) throws IOException {
		StringBuilder buf = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buf.append(line).append(SystemUtils.LINE_SEPARATOR);
			}
		} finally {
			reader.close();
		}
		return buf.toString().trim();
	}

	protected abstract String holidayToExpectedString(Holiday h);

}
