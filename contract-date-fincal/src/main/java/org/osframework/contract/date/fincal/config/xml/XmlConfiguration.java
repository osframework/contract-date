/*
 * File: XmlConfiguration.java
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
package org.osframework.contract.date.fincal.config.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osframework.contract.date.fincal.config.AbstractConcurrentMapConfiguration;
import org.osframework.contract.date.fincal.config.ConfigurationException;

/**
 * XmlConfiguration description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class XmlConfiguration extends AbstractConcurrentMapConfiguration {

	public XmlConfiguration(final File xmlFile) {
		super();
		try {
			load(new FileInputStream(xmlFile));
		} catch (IOException ioe) {
			throw new ConfigurationException("Cannot load Configuration from XML", ioe);
		}
		this.observer.configurationCreated(this);
	}

	/**
	 * Constructor - description here.
	 *
	 */
	public XmlConfiguration(final InputStream xmlIn) {
		super();
		try {
			load(xmlIn);
		} catch (IOException ioe) {
			throw new ConfigurationException("Cannot load Configuration from XML", ioe);
		}
		this.observer.configurationCreated(this);
	}

	public XmlConfiguration(final URL xmlUrl) {
		super();
		try {
			load(xmlUrl.openStream());
		} catch (IOException ioe) {
			throw new ConfigurationException("Cannot load Configuration from XML", ioe);
		}
		this.observer.configurationCreated(this);
	}

	private void load(final InputStream xmlIn) throws IOException {
		// TODO Write this.
	}

}
