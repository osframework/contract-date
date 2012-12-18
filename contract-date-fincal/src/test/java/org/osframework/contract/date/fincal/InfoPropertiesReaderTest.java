package org.osframework.contract.date.fincal;

import static org.testng.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InfoPropertiesReaderTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private InfoPropertiesReader reader = null;
	private String implementationTitle = null, implementationVersion = null;

	@BeforeClass
	private void loadSingleton() {
		reader = InfoPropertiesReader.getInstance();
		implementationTitle = System.getProperty("Implementation-Title");
		implementationVersion = System.getProperty("Implementation-Version");
	}

	@Test
	public void testGetImplementationTitle() {
		String actual = reader.getImplementationTitle();
		assertEquals(actual, implementationTitle);
		logger.debug("Read Implementation-Title: {}", actual);
	}

	@Test
	public void testGetImplementationVersion() {
		String actual = reader.getImplementationVersion();
		assertEquals(actual, implementationVersion);
		logger.debug("Read Implementation-Version: {}", actual);
	}

}
