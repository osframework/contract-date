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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.osframework.contract.date.fincal.config.AbstractConcurrentMapConfiguration;
import org.osframework.contract.date.fincal.config.ConfigurationException;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.osframework.contract.date.fincal.model.HolidayType;
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
		}
		this.observer.configurationCreated(this);
	}

	/**
	 * Constructor - description here.
	 *
	 */
	public XmlConfiguration(final InputStream xmlIn) {
		super();
		load(xmlIn);
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

	protected void load(final InputStream xmlIn) {
		// Copy and close the given input stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IOUtils.copy(xmlIn, baos);
		} catch (IOException ioe) {
			throw new ConfigurationException("Cannot read configuration XML input stream", ioe);
		}
		IOUtils.closeQuietly(xmlIn);
		
		// Create 2 copies of XML input: 1 for validation, 1 for parse
		InputStream validatorIn = new ByteArrayInputStream(baos.toByteArray()),
				    parserIn    = new ByteArrayInputStream(baos.toByteArray());
		
		// Validate configuration XML
		validateConfigurationXml(validatorIn);
		
		// Parse configuration XML
		parseConfigurationXml(parserIn);
	}

	protected ErrorHandler getErrorHandler() {
		return new DefaultLoggingErrorHandler(this.logger);
	}

	protected Reader getXMLReader(final InputStream xmlInput) {
		return IOUtils.toBufferedReader(new InputStreamReader(xmlInput, Charset.forName(XmlConstants.DEFAULT_ENCODING)));
	}

	protected void parseConfigurationXml(final InputStream xmlIn) {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader xsr = null;
		try {
			xsr = xif.createXMLStreamReader(xmlIn);
			int eventType;
			String curEl = null;
			Map<String, Object> constructorArgMap = new HashMap<String, Object>();
			StringBuilder curFieldValueBuf = new StringBuilder();
			while (xsr.hasNext()) {
				xsr.next();
				eventType = xsr.getEventType();
				switch (eventType) {
				case XMLStreamConstants.START_ELEMENT:
					curEl = xsr.getLocalName();
					// Skip container elements
					if (isContainerElement(curEl)) {
						;;
					} else if (isDefinitionEntityElement(curEl)){
						constructorArgMap.clear();
						// On start of new financial calendar, re-initialize
						// placeholders for constructor args to be built by
						// reference to prior definition entities
						if (XmlConstants.ELEMENT_CALENDAR.equals(curEl)) {
							constructorArgMap.put(XmlConstants.ELEMENT_CENTRALBANK, null);
							constructorArgMap.put(XmlConstants.ELEMENT_HOLIDAYS, new HashSet<HolidayDefinition>());
						}
						logger.debug("New '{}' configuration definition", curEl);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					curEl = xsr.getLocalName();
					// Skip container elements
					if (isContainerElement(curEl)) {
						;;
					} else if (isDefinitionEntityElement(curEl)){
						addDefinitionEntity(curEl, constructorArgMap);
						logger.debug("Added '{}' configuration definition", curEl);
					} else {
						putDefinitionEntityArg(curEl, curFieldValueBuf.toString().trim(), constructorArgMap);
						curFieldValueBuf.setLength(0);
					}
					curEl = null;
					break;
				case XMLStreamConstants.ATTRIBUTE:
					String attrLocalName = xsr.getAttributeLocalName(0);
					String attrValue = xsr.getAttributeValue(0);
					if (XmlConstants.ATTRIBUTE_ID.equals(attrLocalName)) {
						putDefinitionEntityArg(attrLocalName, attrValue, constructorArgMap);
					} else if (XmlConstants.ATTRIBUTE_REFID.equals(attrLocalName)) {
						putDefinitionEntityArgByRef(curEl, attrValue, constructorArgMap);
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					curFieldValueBuf.append(xsr.getText());
					break;
				}
			}
		} catch (XMLStreamException xse) {
			throw new ConfigurationException("Cannot parse configuration XML", xse);
		} finally {
			// Close XMLStreamReader 1st
			if (null != xsr) {
				try {
					xsr.close();
				} catch (XMLStreamException xse) {
					throw new ConfigurationException("Failed to close configuration XML stream", xse);
				}
			}
			// Close XML input stream
			IOUtils.closeQuietly(xmlIn);
		}
	}

	protected void validateConfigurationXml(final InputStream xmlIn) {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema xsd = sf.newSchema(this.getClass().getResource(XmlConstants.XSD_FILENAME));
			Validator validator = xsd.newValidator();
			validator.setErrorHandler(this.getErrorHandler());
			validator.validate(new StreamSource(xmlIn));
		} catch (IOException ioe) {
			throw new ConfigurationException("Configuration XML validation failure", ioe);
		} catch (SAXException se) {
			throw new ConfigurationException("Configuration XML validation failure", se);
		} finally {
			// Close XML input stream
			IOUtils.closeQuietly(xmlIn);
		}
	}

	protected final boolean isContainerElement(String elLocalName) {
		return (XmlConstants.ELEMENT_DEFINITIONS.equals(elLocalName) ||
				XmlConstants.ELEMENT_CENTRALBANKS.equals(elLocalName) ||
				XmlConstants.ELEMENT_HOLIDAYS.equals(elLocalName) ||
				XmlConstants.ELEMENT_CALENDARS.equals(elLocalName));
	}

	protected final boolean isDefinitionEntityElement(String elLocalName) {
		return (XmlConstants.ELEMENT_CENTRALBANK.equals(elLocalName) ||
				XmlConstants.ELEMENT_HOLIDAY.equals(elLocalName) ||
				XmlConstants.ELEMENT_CALENDAR.equals(elLocalName));
	}

	protected final void putDefinitionEntityArg(String elLocalName, String strValue, Map<String, Object> constructorArgMap) {
		Object argValue = null;
		if (XmlConstants.ELEMENT_OBSERVANCE.equals(elLocalName)) {
			argValue = HolidayType.valueOf(strValue);
		} else if (XmlConstants.ELEMENT_CURRENCY.equals(elLocalName)) {
			argValue = Currency.getInstance(strValue);
		} else {
			argValue = strValue;
		}
		constructorArgMap.put(elLocalName, argValue);
	}

	@SuppressWarnings("unchecked")
	protected final void putDefinitionEntityArgByRef(String elLocalName, String refId, Map<String, Object> constructorArgMap) {
		if (XmlConstants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			CentralBank cb = getCentralBank(refId);
			constructorArgMap.put(elLocalName, cb);
		} else if (XmlConstants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			HolidayDefinition hd = getHolidayDefinition(refId);
			((Set<HolidayDefinition>)constructorArgMap.get(XmlConstants.ELEMENT_HOLIDAYS)).add(hd);
		}
	}

	@SuppressWarnings("unchecked")
	protected final void addDefinitionEntity(String elLocalName, Map<String, Object> constructorArgMap) {
		final Object[] args;
		if (XmlConstants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_NAME),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_COUNTRY),
				(Currency)constructorArgMap.get(XmlConstants.ELEMENT_CURRENCY)
			};
			CentralBank cb = (CentralBank)constructDefinitionEntity(CentralBank.class, args);
			addCentralBank(cb);
		} else if (XmlConstants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_NAME),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_DESCRIPTION),
				(HolidayType)constructorArgMap.get(XmlConstants.ELEMENT_OBSERVANCE),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_EXPRESSION),
			};
			HolidayDefinition hd = (HolidayDefinition)constructDefinitionEntity(HolidayDefinition.class, args);
			addHolidayDefinition(hd);
		} else if (XmlConstants.ELEMENT_CALENDAR.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_DESCRIPTION),
				(CentralBank)constructorArgMap.get(XmlConstants.ELEMENT_CENTRALBANK),
				(Set<HolidayDefinition>)constructorArgMap.get(XmlConstants.ELEMENT_HOLIDAYS)
			};
			FinancialCalendar fc = (FinancialCalendar)constructDefinitionEntity(FinancialCalendar.class, args);
			addFinancialCalendar(fc);
		}
	}

	private Object constructDefinitionEntity(Class<?> cls, Object[] args) {
		try {
			return ConstructorUtils.invokeConstructor(cls, args);
		} catch (Exception e) {
			throw new ConfigurationException("Cannot construct configuration entity of type: " + cls.getName(), e);
		}
	}

	protected class DefaultLoggingErrorHandler implements ErrorHandler {
	
		private final Logger logger;
	
		private DefaultLoggingErrorHandler(Logger logger) {
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
