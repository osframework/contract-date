/*
 * File: DefaultXmlConfiguration.java
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
import java.util.ArrayDeque;
import java.util.Currency;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.osframework.contract.date.fincal.config.AbstractConcurrentMapConfiguration;
import org.osframework.contract.date.fincal.config.ConfigurationException;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.osframework.contract.date.fincal.model.HolidayType;
import org.xml.sax.ErrorHandler;

/**
 * Default implementation of <code>Configuration</code> which loads from an XML
 * document. Instances of this class cache definition entities as they are
 * parsed from the XML source provided at construction.
 * <p>
 * This class uses the
 * <a href="http://docs.oracle.com/cd/E17802_01/webservices/webservices/docs/1.6/tutorial/doc/SJSXP3.html">StAX API</a>
 * for XML parsing. Subclasses may override this to employ their own parsing
 * strategy.
 * </p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class DefaultXmlConfiguration extends AbstractConcurrentMapConfiguration {

	/**
	 * Construct new instance of <code>DefaultXmlConfiguration</code> from an XML input
	 * stream. Actual loading of this object from the specified input stream is
	 * delegated to the {@link #load(InputStream)} method.
	 *
	 * @param xmlIn XML configuration input stream
	 * @throws ConfigurationException if configuration fails for any reason
	 */
	public DefaultXmlConfiguration(final InputStream xmlIn) {
		super();
		initializeObserverChain();
		load(xmlIn);
		observer.configurationCreated(this);
	}

	/**
	 * Construct new instance of <code>DefaultXmlConfiguration</code> from an XML file.
	 *  Actual loading of this object from the specified input stream is
	 *  delegated to the {@link #load(InputStream)} method.
	 * 
	 * @param xmlFile XML configuration file
	 * @throws ConfigurationException if configuration fails for any reason
	 * @throws IOException if file cannot be opened for reading
	 * @throws SecurityException if a security manager denies read access to the file
	 */
	public DefaultXmlConfiguration(final File xmlFile) throws IOException {
		this(new FileInputStream(xmlFile));
	}

	/**
	 * Construct new instance of <code>DefaultXmlConfiguration</code> from an XML
	 * resource. Actual loading of this object from the specified input stream
	 * is delegated to the {@link #load(InputStream)} method.
	 *
	 * @param xmlUrl URL of XML configuration resource
	 * @throws ConfigurationException if configuration fails for any reason
	 * @throws IOException if connection cannot be opened to URL for reading
	 */
	public DefaultXmlConfiguration(final URL xmlUrl) throws IOException {
		this(xmlUrl.openStream());
	}

	/**
	 * Load this configuration object from the specified XML input stream. This
	 * method is responsible for the actual XML validation and parsing to which
	 * constructors delegate responsibility.
	 * <p>
	 * This method is <code>final</code>; subclasses cannot override it. For
	 * customization of XML validation and parsing, subclasses should override
	 * the {@link #validateConfiguration(InputStream)} and
	 * {@link #parseConfiguration(InputStream)} methods.
	 * </p>
	 * 
	 * @param xmlIn XML configuration input stream
	 */
	protected final void load(final InputStream xmlIn) {
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
		validateConfiguration(validatorIn);
		
		// Parse configuration XML
		parseConfiguration(parserIn);
	}

	/**
	 * Get the <code>ErrorHandler</code> object used in XML validation and/or
	 * parsing. This implementation provides an instance of
	 * <code>DefaultErrorHandler</code>.
	 * <p>Subclasses may override this method.</p>
	 * 
	 * @return error handler or <code>null</code>
	 * @see DefaultErrorHandler
	 */
	protected ErrorHandler getErrorHandler() {
		return new DefaultErrorHandler(this.logger);
	}

	/**
	 * Parse the XML input stream and add constructed definition entities to
	 * this configuration as they are constructed.
	 * <p>Subclasses may override this method.</p>
	 * 
	 * @param xmlIn XML configuration input stream
	 * @throws ConfigurationException if parsing fails for any reason
	 */
	protected void parseConfiguration(final InputStream xmlIn) {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader xsr = null;
		try {
			xsr = xif.createXMLStreamReader(xmlIn);
			int eventType;
			Deque<StackElement> elementStack = new ArrayDeque<StackElement>();
			StackElement curEl = null;
			Map<String, Object> constructorArgMap = new HashMap<String, Object>();
			StringBuilder curFieldValueBuf = new StringBuilder();
			while (xsr.hasNext()) {
				xsr.next();
				eventType = xsr.getEventType();
				switch (eventType) {
				case XMLStreamConstants.START_ELEMENT:
					String elementName = xsr.getLocalName();
					String elementId = ((1 == xsr.getAttributeCount()) && XmlConstants.ATTRIBUTE_ID.equals(xsr.getAttributeLocalName(0)))
							            ? xsr.getAttributeValue(0)
							            : null;
					String elementRefId = ((1 == xsr.getAttributeCount()) && XmlConstants.ATTRIBUTE_REFID.equals(xsr.getAttributeLocalName(0)))
							               ? xsr.getAttributeValue(0)
							               : null;
					curEl = new StackElement(elementName, elementId, elementRefId);
					elementStack.push(curEl);
					// Skip container elements
					if (isEntityElement(curEl)){
						logger.debug("Started '{}' configuration definition", curEl.getElementName());
						constructorArgMap.clear();
						putEntityConstructorArg(XmlConstants.ATTRIBUTE_ID, curEl.getElementId(), constructorArgMap);
					} else if (isEntityReference(curEl)) {
						putEntityConstructorArgByRef(curEl.getElementName(), curEl.getElementRefId(), constructorArgMap);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					curEl = elementStack.pop();
					// Skip container elements
					if (isEntityElement(curEl)) {
						addEntity(curEl.getElementName(), constructorArgMap);
						logger.debug("Ended '{}' configuration definition", curEl.getElementName());
					} else if (!isContainerElement(curEl) && !isEntityReference(curEl)){
						putEntityConstructorArg(curEl.getElementName(), curFieldValueBuf.toString().trim(), constructorArgMap);
						curFieldValueBuf.setLength(0);
					}
					curEl = null;
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

	/**
	 * Validate the XML input stream and throw a
	 * <code>ConfigurationException</code> if invalid. This method is called
	 * prior to {@link #parseConfiguration(InputStream)}.
	 * <p>Subclasses may override this method.</p>
	 * 
	 * @param xmlIn XML configuration input stream
	 * @throws ConfigurationException if XML is invalid
	 */
	protected void validateConfiguration(final InputStream xmlIn) {
		Schema schema = XmlConstants.getSchema();
		Validator validator = schema.newValidator();
		validator.setErrorHandler(getErrorHandler());
		try {
			validator.validate(new StreamSource(xmlIn));
		} catch (Exception e) {
			throw new ConfigurationException("Configuration XML validation failure", e);
		} finally {
			// Close XML input stream
			IOUtils.closeQuietly(xmlIn);
		}
	}

	private boolean isContainerElement(StackElement curEl) {
		return (XmlConstants.ELEMENT_DEFINITIONS.equals(curEl.getElementName()) ||
				XmlConstants.ELEMENT_CENTRALBANKS.equals(curEl.getElementName()) ||
				XmlConstants.ELEMENT_HOLIDAYS.equals(curEl.getElementName()) ||
				XmlConstants.ELEMENT_CALENDARS.equals(curEl.getElementName()));
	}

	private boolean isEntityElement(StackElement curEl) {
		return ((XmlConstants.ELEMENT_CENTRALBANK.equals(curEl.getElementName()) ||
				 XmlConstants.ELEMENT_HOLIDAY.equals(curEl.getElementName()) ||
				 XmlConstants.ELEMENT_CALENDAR.equals(curEl.getElementName())) &&
				(null != curEl.getElementId()));
	}

	private boolean isEntityReference(StackElement curEl) {
		return ((XmlConstants.ELEMENT_CENTRALBANK.equals(curEl.getElementName()) ||
				 XmlConstants.ELEMENT_HOLIDAY.equals(curEl.getElementName())) &&
				(null != curEl.getElementRefId()));
	}

	private void putEntityConstructorArg(String elLocalName, String strValue, Map<String, Object> constructorArgMap) {
		Object argValue = null;
		if (XmlConstants.ELEMENT_OBSERVANCE.equals(elLocalName)) {
			argValue = HolidayType.valueOf(strValue);
		} else if (XmlConstants.ELEMENT_CURRENCY.equals(elLocalName)) {
			argValue = Currency.getInstance(strValue);
		} else {
			argValue = strValue;
		}
		constructorArgMap.put(elLocalName, argValue);
		logger.debug("Added constructor arg: '{}' = {}", elLocalName, argValue);
	}

	@SuppressWarnings("unchecked")
	private void putEntityConstructorArgByRef(String elLocalName, String refId, Map<String, Object> constructorArgMap) {
		if (XmlConstants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			CentralBank cb = getCentralBank(refId);
			constructorArgMap.put(elLocalName, cb);
			logger.debug("Put referenced CentralBank in constructor args: {}", cb);
		} else if (XmlConstants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			HolidayDefinition hd = getHolidayDefinition(refId);
			Set<HolidayDefinition> holidays = (Set<HolidayDefinition>)constructorArgMap.get(XmlConstants.ELEMENT_HOLIDAYS);
			if (null == holidays) {
				holidays = new HashSet<HolidayDefinition>();
			}
			holidays.add(hd);
			constructorArgMap.put(XmlConstants.ELEMENT_HOLIDAYS, holidays);
			logger.debug("Added referenced HolidayDefinition to constructor args: {}", hd);
		}
	}

	@SuppressWarnings("unchecked")
	private void addEntity(String elLocalName, Map<String, Object> constructorArgMap) {
		final Object[] args;
		final Class<?>[] argTypes;
		if (XmlConstants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_NAME),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_COUNTRY),
				(Currency)constructorArgMap.get(XmlConstants.ELEMENT_CURRENCY)
			};
			argTypes = new Class<?>[]{
				String.class,
				String.class,
				String.class,
				Currency.class
			};
			CentralBank cb = (CentralBank)constructDefinitionEntity(CentralBank.class, args, argTypes);
			addCentralBank(cb);
			logger.debug("Added CentralBank: {}", cb);
		} else if (XmlConstants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_NAME),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_DESCRIPTION),
				(HolidayType)constructorArgMap.get(XmlConstants.ELEMENT_OBSERVANCE),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_EXPRESSION),
			};
			argTypes = new Class<?>[] {
				String.class,
				String.class,
				String.class,
				HolidayType.class,
				String.class
			};
			HolidayDefinition hd = (HolidayDefinition)constructDefinitionEntity(HolidayDefinition.class, args, argTypes);
			addHolidayDefinition(hd);
			logger.debug("Added HolidayDefinition: {}", hd);
		} else if (XmlConstants.ELEMENT_CALENDAR.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(XmlConstants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(XmlConstants.ELEMENT_DESCRIPTION),
				(CentralBank)constructorArgMap.get(XmlConstants.ELEMENT_CENTRALBANK),
				(Set<HolidayDefinition>)constructorArgMap.get(XmlConstants.ELEMENT_HOLIDAYS)
			};
			argTypes = new Class<?>[] {
				String.class,
				String.class,
				CentralBank.class,
				Set.class
			};
			FinancialCalendar fc = (FinancialCalendar)constructDefinitionEntity(FinancialCalendar.class, args, argTypes);
			addFinancialCalendar(fc);
			logger.debug("Added FinancialCalendar: {}", fc);
		}
	}

	private Object constructDefinitionEntity(Class<?> cls, Object[] args, Class<?>[] argTypes) {
		try {
			return ConstructorUtils.invokeConstructor(cls, args, argTypes);
		} catch (Exception e) {
			throw new ConfigurationException("Cannot construct configuration entity of type: " + cls.getName(), e);
		}
	}

}
