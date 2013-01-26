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

import org.osframework.contract.date.fincal.config.Configuration;
import org.osframework.contract.date.fincal.config.xml.DefaultXmlConfiguration;
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
	public void testAddCalendarInvalidCalendar() {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.addCalendar("XYZ");
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="generator",
		  dependsOnGroups={"config","data","producer"},
		  expectedExceptions=FinancialCalendarException.class)
	public void testBuildNoCalendars() throws FinancialCalendarException {
		FinancialCalendarGeneratorBuilder builder = new FinancialCalendarGeneratorBuilder(config);
		builder.build();
		fail("Expected FinancialCalendarException to be thrown");
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

}
