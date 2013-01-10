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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.osframework.contract.date.fincal.config.AbstractConcurrentMapConfiguration;
import org.osframework.contract.date.fincal.config.ConfigurationException;
import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
		} catch (SAXException se) {
			throw new ConfigurationException("Cannot parse XML", se);
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
		} catch (SAXException se) {
			throw new ConfigurationException("Cannot parse XML", se);
		}
		this.observer.configurationCreated(this);
	}

	public XmlConfiguration(final URL xmlUrl) {
		super();
		try {
			load(xmlUrl.openStream());
		} catch (IOException ioe) {
			throw new ConfigurationException("Cannot load Configuration from XML", ioe);
		} catch (SAXException se) {
			throw new ConfigurationException("Cannot parse XML", se);
		}
		this.observer.configurationCreated(this);
	}

	private void load(final InputStream xmlIn) throws IOException, SAXException {
		// Copy and close the given input stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(xmlIn, baos);
		IOUtils.closeQuietly(xmlIn);
		
		// Create 2 copies of XML input: 1 for validation, 1 for parse
		InputStream validatorIn = new ByteArrayInputStream(baos.toByteArray()),
				    parserIn    = new ByteArrayInputStream(baos.toByteArray());
		
		// Validate configuration XML
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema xsd = sf.newSchema(this.getClass().getResource(XmlConstants.XSD_FILENAME));
		Validator validator = xsd.newValidator();
		validator.setErrorHandler(new ValidationErrorHandler(this.logger));
		validator.validate(new StreamSource(validatorIn));
		
		// Parse configuration XML
	}

	private class ValidationErrorHandler implements ErrorHandler {
	
		private final Logger logger;
	
		private ValidationErrorHandler(Logger logger) {
			this.logger = logger;
		}
	
		@Override
		public void warning(SAXParseException exception) throws SAXException {
			logger.warn("Validation Warning", exception);
			throw exception;
		}

		@Override
		public void error(SAXParseException exception) throws SAXException {
			logger.error("Validation Error", exception);
			throw exception;
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			logger.error("Validation Fatal Error", exception);
			throw exception;
		}
		
	}
}
