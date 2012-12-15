package org.osframework.contract.date.fincal;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ManifestReaderTest {

	String EXPECTED_TITLE = "contract-date-fincal";
	String EXPECTED_VERSION = "0.0.1-SNAPSHOT";

	private ManifestReader reader = null;

	@BeforeClass
	private void initManifestReader() {
		reader = new ManifestReader();
	}

	@Test
	public void testGetImplementationTitle() {
		String title = reader.getImplementationTitle();
		assertNotNull(title);
		assertEquals(title, EXPECTED_TITLE);
	}

	@Test
	public void testGetImplementationVersion() {
		String version = reader.getImplementationVersion();
		assertNotNull(version);
		assertEquals(version, EXPECTED_VERSION);
	}

}
