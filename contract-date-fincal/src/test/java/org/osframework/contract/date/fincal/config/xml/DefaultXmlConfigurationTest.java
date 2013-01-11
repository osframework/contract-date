package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class DefaultXmlConfigurationTest {

	private URL definitionXml = null;
	private InputStream xmlIn = null;

	@BeforeClass
	public void beforeClass() {
		definitionXml = this.getClass().getResource("/definitions-test.xml");
		assertNotNull(definitionXml);
	}

	@BeforeMethod
	public void beforeMethod() throws IOException {
		xmlIn = definitionXml.openStream();
	}

	@AfterMethod
	public void afterMethod() {
		xmlIn = null;
	}

	@Test
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
