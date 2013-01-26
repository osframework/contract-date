/*
 * File: FinancialCalendarGeneratorBuilderTest.java
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
package org.osframework.contract.date.fincal;

import static org.osframework.contract.date.fincal.ObjectMother.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang.math.IntRange;
import org.osframework.contract.date.fincal.config.Configuration;
import org.osframework.contract.date.fincal.config.xml.DefaultXmlConfiguration;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 * Unit tests for <code>FinancialCalendarGeneratorBuilder</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FinancialCalendarGeneratorBuilderTest {

	private Configuration config = null;

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  expectedExceptions=IllegalArgumentException.class)
	public void testConstructorNullArg() {
		new FinancialCalendarGeneratorBuilder(null);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  expectedExceptions=IllegalArgumentException.class)
	public void testAddInvalidCalendar() {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.addCalendar("XYZ");
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="calendarIds")
	public void testAddCalendar(String calendarId) {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.addCalendar(calendarId);
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="invalidYears",
		  expectedExceptions=IllegalArgumentException.class)
	public void testSetInvalidFirstYear(int year) {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.setFirstYear(year);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="validYears")
	public void testSetFirstYear(int year) {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.setFirstYear(year);
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="invalidYears",
		  expectedExceptions=IllegalArgumentException.class)
	public void testSetInvalidLastYear(int year) {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.setLastYear(year);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="validYears")
	public void testSetLastYear(int year) {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.setLastYear(year);
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  expectedExceptions=FinancialCalendarException.class)
	public void testBuildNoCalendars() throws FinancialCalendarException {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.build();
		fail("Expected FinancialCalendarException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  dataProvider="builderConfigParams")
	public void testBuild(String[] calendarIds,
			              int firstYear,
			              int lastYear,
			              boolean weekends,
			              Class<?> expectedType) throws FinancialCalendarException {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		// Add all calendars
		for (String calendarId : calendarIds) {
			builder.addCalendar(calendarId);
		}
		builder.setFirstYear(firstYear).setLastYear(lastYear).includeWeekends(weekends);
		FinancialCalendarGenerator generator = builder.build();
		assertEquals(generator.getClass(), expectedType);
	}

	@BeforeClass
	public void beforeClass() throws IOException {
		URL configXml = loadDefinitionsXML();
		this.config = new DefaultXmlConfiguration(configXml);
	}

	@AfterClass
	public void afterClass() {
		this.config = null;
	}

	@DataProvider
	public Object[][] calendarIds() {
		Iterator<FinancialCalendar> it = config.financialCalendarIterator();
		List<String> calendarIds = new ArrayList<String>();
		while (it.hasNext()) {
			calendarIds.add(it.next().getId());
		}
		Object[][] results = new Object[calendarIds.size()][];
		for (int i = 0; i < calendarIds.size(); i++) {
			results[i] = new Object[] { calendarIds.get(i) };
		}
		return results;
	}

	@DataProvider
	public Object[][] invalidYears() {
		return new Object[][] {
			new Object[] { 1899 },
			new Object[] { 5000 }
		};
	}

	@DataProvider
	public Iterator<Object[]> validYears() {
		final IntRange years = new IntRange(1900, 4099);
		return new Iterator<Object[]>() {
			int next = years.getMinimumInteger();
			public boolean hasNext() {
				return (years.containsInteger(next));
			}
			public Object[] next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return new Object[] { next++ };
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@DataProvider
	public Object[][] builderConfigParams() {
		Object[] set1 = new Object[] {
			new String[] { "NYB", "LnB" },
			2012,
			2061,
			true,
			ConcurrentFinancialCalendarGenerator.class
		};
		Object[] set2 = new Object[] {
			new String[] { "NYB", "LnB" },
			2012,
			2021,
			false,
			SequentialFinancialCalendarGenerator.class
		};
		return new Object[][] {
			set1, set2
		};
	}

}
