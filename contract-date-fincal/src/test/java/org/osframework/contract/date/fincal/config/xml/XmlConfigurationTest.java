package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.*;

import java.net.URL;
import java.util.Iterator;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class XmlConfigurationTest {

	private URL definitionXml = null;

	@BeforeClass
	public void beforeClass() {
		definitionXml = this.getClass().getResource("/definitions-test.xml");
		assertNotNull(definitionXml);
	}

	@Test
	public void testConstructorURL() {
		XmlConfiguration config = new XmlConfiguration(definitionXml);
		Iterator<CentralBank> cbIt = config.centralBankIterator();
		assertTrue(cbIt.hasNext());
		
		Iterator<HolidayDefinition> hdIt = config.holidayDefinitionIterator();
		assertTrue(hdIt.hasNext());
		
		Iterator<FinancialCalendar> fcIt = config.financialCalendarIterator();
		assertTrue(fcIt.hasNext());
	}

}
