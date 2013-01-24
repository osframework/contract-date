/*
 * File: DefaultXmlConfigurationTest.java
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
package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.osframework.contract.date.fincal.ObjectMother;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>DefaultXmlConfiguration</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class DefaultXmlConfigurationTest {

	private InputStream xmlIn = null;

	@BeforeMethod(groups = {"config"})
	public void beforeMethod() throws IOException {
		xmlIn = ObjectMother.loadDefinitionsXML().openStream();
	}

	@AfterMethod
	public void afterMethod() {
		xmlIn = null;
	}

	@Test(groups = {"config"},
		  dependsOnGroups = {"model"})
	public void testConstructorURL() {
		DefaultXmlConfiguration config = new DefaultXmlConfiguration(xmlIn);
		Iterator<CentralBank> cbIt = config.centralBankIterator();
		assertTrue(cbIt.hasNext());
		
		Iterator<HolidayDefinition> hdIt = config.holidayDefinitionIterator();
		assertTrue(hdIt.hasNext());
		
		Iterator<FinancialCalendar> fcIt = config.financialCalendarIterator();
		assertTrue(fcIt.hasNext());
	}

}
