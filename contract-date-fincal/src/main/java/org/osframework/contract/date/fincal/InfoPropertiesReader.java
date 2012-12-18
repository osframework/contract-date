/*
 * File: InfoPropertiesReader.java
 * 
 * Copyright 2012 OSFramework Project.
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

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.osframework.contract.date.ProjectInfoReader;

/**
 * Singleton reader that reads project info from this artifact's
 * <code>info.properties</code> resource. Package private; used exclusively by
 * Main.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class InfoPropertiesReader implements ProjectInfoReader {

	private Properties infoProperties = null;

	private InfoPropertiesReader() {
		this.infoProperties = new Properties();
		URL propsURL = this.getClass().getResource("/info.properties");
		if (null != propsURL) {
			try {
				infoProperties.load(propsURL.openStream());
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		}
	}

	public String getImplementationTitle() {
		return infoProperties.getProperty(KEY_IMPLEMENTATION_TITLE);
	}

	public String getImplementationVersion() {
		return infoProperties.getProperty(KEY_IMPLEMENTATION_VERSION);
	}

	public String getImplementationVendor() {
		return infoProperties.getProperty(KEY_IMPLEMENTATION_VENDOR);
	}

	public static InfoPropertiesReader getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static final class SingletonHolder {
		static final InfoPropertiesReader INSTANCE = new InfoPropertiesReader();
	}

}
