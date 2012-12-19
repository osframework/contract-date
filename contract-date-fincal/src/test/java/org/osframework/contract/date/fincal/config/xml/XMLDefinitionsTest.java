package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.assertNotNull;

import java.net.URL;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class XMLDefinitionsTest {

	private static final String DEFAULT_DEFINITIONS_PATH = "/definitions.default.xml";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String expectedCentralBank = "SNB";
	private String expectedHoliday = "NewYearsDay";
	private String expectedFinancialCalendar = "NCH";

	@Test(groups = "config")
	public void testLoad() throws Exception {
		String xmlFilePath = definitionsAsFilePath();
		logger.debug("Loaded XML filepath: {}", xmlFilePath);
		XMLDefinitions definitions = new XMLDefinitions(xmlFilePath);
		definitions.load();
		logger.debug("Loaded definitions: {}", !definitions.isEmpty());
		logger.debug("Checksum: {}", definitions.getChecksum());
		assertExpectedDefinitions(definitions);
	}

	private String definitionsAsFilePath() {
		URL url = this.getClass().getResource(DEFAULT_DEFINITIONS_PATH);
		assertNotNull(url);
		return url.toString();
	}

	private void assertExpectedDefinitions(XMLDefinitions definitions) {
		CentralBank actualCentralBank = definitions.getCentralBank(expectedCentralBank);
		assertNotNull(actualCentralBank);
		logger.debug("Loaded CentralBank: {}", actualCentralBank);
		
		HolidayDefinition actualHoliday = definitions.getHolidayDefinition(expectedHoliday);
		assertNotNull(actualHoliday);
		logger.debug("Loaded HolidayDefinition: {}", actualHoliday);
		
		FinancialCalendar actualFinancialCalendar = definitions.getFinancialCalendar(expectedFinancialCalendar);
		assertNotNull(actualFinancialCalendar);
		logger.debug("Loaded FinancialCalendar: {}", actualFinancialCalendar);
	}

}