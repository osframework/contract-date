package org.osframework.contract.date.fincal.config.bdb;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class BerkeleyDBDefinitionsTest {

	private static final String DEFAULT_DEFINITIONS_PATH = BerkeleyDBDefinitions.HOME_DIRECTORY_DEFAULT;

	@Test(groups = "config")
	public void testLoad() throws Exception {
		BerkeleyDBDefinitions definitions = new BerkeleyDBDefinitions(DEFAULT_DEFINITIONS_PATH);
		definitions.load();
	}

	@AfterClass
	private void deleteDbDirectory() {
		File dbDir = new File(DEFAULT_DEFINITIONS_PATH);
		if (dbDir.isDirectory()) {
			File[] files = dbDir.listFiles();
			for (File f : files) {
				f.delete();
			}
		}
		dbDir.delete();
	}

}
