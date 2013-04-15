/*
 * File: XmlDefinitionSource.java
 * 
 * Copyright 2013 OSFramework Project.
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
package org.osframework.contract.date.fincal.definition.source.xml;

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
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.definition.HolidayType;
import org.osframework.contract.date.fincal.definition.source.DefinitionSource;
import org.osframework.contract.date.fincal.definition.source.DefinitionSourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>DefinitionSource</code> created from an XML document. Instances of this
 * class cache definition entities as they are parsed from the XML source
 * provided at construction.
 * <p>
 * This class uses the <a href="http://en.wikipedia.org/wiki/StAX">StAX API</a>
 * for XML parsing.</p>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class XmlDefinitionSource implements DefinitionSource {

	protected final ConcurrentMap<String, CentralBank> centralBanks = new ConcurrentHashMap<String, CentralBank>();
	protected final ConcurrentMap<String, HolidayDefinition> holidayDefinitions = new ConcurrentHashMap<String, HolidayDefinition>();
	protected final ConcurrentMap<String, FinancialCalendar> financialCalendars = new ConcurrentHashMap<String, FinancialCalendar>();

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected final class CentralBankIterator implements Iterator<CentralBank> {
	
		final Iterator<String> centralBankIdIt;
	
		protected CentralBankIterator() {
			this.centralBankIdIt = centralBanks.keySet().iterator();
		}
	
		public boolean hasNext() {
			return centralBankIdIt.hasNext();
		}
	
		public CentralBank next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return centralBanks.get(centralBankIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	protected final class HolidayDefinitionIterator implements Iterator<HolidayDefinition> {
	
		final Iterator<String> holidayDefIdIt;
	
		protected HolidayDefinitionIterator() {
			this.holidayDefIdIt = holidayDefinitions.keySet().iterator();
		}
	
		public boolean hasNext() {
			return holidayDefIdIt.hasNext();
		}
	
		public HolidayDefinition next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return holidayDefinitions.get(holidayDefIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	protected final class FinancialCalendarIterator implements Iterator<FinancialCalendar> {
	
		final Iterator<String> finCalIdIt;
	
		protected FinancialCalendarIterator() {
			this.finCalIdIt = financialCalendars.keySet().iterator();
		}
	
		public boolean hasNext() {
			return finCalIdIt.hasNext();
		}
	
		public FinancialCalendar next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return financialCalendars.get(finCalIdIt.next());
		}
	
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 
	 */
	public XmlDefinitionSource(final URL definitionXmlURL) {
		Validate.notNull(definitionXmlURL, "definitionXML URL argument cannot be null");
		try {
			load(definitionXmlURL.openStream());
		} catch (IOException ioe) {
			throw new DefinitionSourceException("Cannot open stream to definition XML at URL " +
		                                        definitionXmlURL.toString(), ioe);
		}
	}

	public XmlDefinitionSource(final File definitionXmlFile) {
		Validate.notNull(definitionXmlFile, "definitionXML file argument cannot be null");
		try {
			load(new FileInputStream(definitionXmlFile));
		} catch (IOException ioe) {
			throw new DefinitionSourceException("Cannot open stream to definition XML at file " +
		                                        definitionXmlFile.getAbsolutePath(), ioe);
		}
	}

	/**
	 * Get the cached <code>CentralBank</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public CentralBank getCentralBank(String centralBankId) {
		return centralBanks.get(centralBankId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>CentralBank</code> objects.
	 */
	public Iterator<CentralBank> centralBankIterator() {
		return new CentralBankIterator();
	}

	/**
	 * Get the cached <code>HolidayDefinition</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public HolidayDefinition getHolidayDefinition(String holidayDefinitionId) {
		return holidayDefinitions.get(holidayDefinitionId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>HolidayDefinition</code> objects.
	 */
	public Iterator<HolidayDefinition> holidayDefinitionIterator() {
		return new HolidayDefinitionIterator();
	}

	/**
	 * Get the cached <code>FinancialCalendar</code> object with the specified
	 * unique ID.
	 * 
	 * @return element cached with the given ID, or <code>null</code> if ID was
	 *         not located
	 */
	public FinancialCalendar getFinancialCalendar(String financialCalendarId) {
		return financialCalendars.get(financialCalendarId);
	}

	/**
	 * @return thread-safe, read-only iterator over cached
	 * <code>FinancialCalendar</code> objects.
	 */
	public Iterator<FinancialCalendar> financialCalendarIterator() {
		return new FinancialCalendarIterator();
	}

	/**
	 * Parse the XML input stream and add definitions to this source as they
	 * are constructed.
	 * <p>Subclasses may override this method.</p>
	 * 
	 * @param xmlIn definition XML input stream
	 * @throws DefinitionSourceException if parsing fails for any reason
	 */
	protected void parseDefinitionXml(final InputStream xmlIn) {
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
					String elementId = ((1 == xsr.getAttributeCount()) && Constants.ATTRIBUTE_ID.equals(xsr.getAttributeLocalName(0)))
							            ? xsr.getAttributeValue(0)
							            : null;
					String elementRefId = ((1 == xsr.getAttributeCount()) && Constants.ATTRIBUTE_REFID.equals(xsr.getAttributeLocalName(0)))
							               ? xsr.getAttributeValue(0)
							               : null;
					curEl = new StackElement(elementName, elementId, elementRefId);
					elementStack.push(curEl);
					// Skip container elements
					if (isEntityElement(curEl)){
						logger.debug("Started '{}' configuration definition", curEl.getElementName());
						constructorArgMap.clear();
						putEntityConstructorArg(Constants.ATTRIBUTE_ID, curEl.getElementId(), constructorArgMap);
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
			throw new DefinitionSourceException("Cannot parse definition XML", xse);
		} finally {
			// Close XMLStreamReader 1st
			if (null != xsr) {
				try {
					xsr.close();
				} catch (XMLStreamException xse) {
					throw new DefinitionSourceException("Failed to close definition XML stream", xse);
				}
			}
			// Close XML input stream
			IOUtils.closeQuietly(xmlIn);
		}
	}

	/**
	 * Validate the XML input stream and throw a runtime exception if invalid.
	 * This method is called prior to {@link #parseDefinitionXml(InputStream)}.
	 * <p>Subclasses may override this method.</p>
	 * 
	 * @param xmlIn definition XML input stream
	 * @throws DefinitionSourceException if XML is invalid
	 */
	protected void validateDefinitionXml(final InputStream xmlIn) {
		Schema schema = Constants.getSchema();
		Validator validator = schema.newValidator();
		try {
			validator.validate(new StreamSource(xmlIn));
		} catch (Exception e) {
			throw new DefinitionSourceException("Definition XML validation failure", e);
		} finally {
			// Close XML input stream
			IOUtils.closeQuietly(xmlIn);
		}
	}

	protected void addCentralBank(CentralBank centralBank) {
		if (null == centralBanks.putIfAbsent(centralBank.getId(), centralBank)) {
			logger.debug("Added CentralBank: {}", centralBank);
		}
	}

	protected void addFinancialCalendar(FinancialCalendar financialCalendar) {
		if (null == financialCalendars.putIfAbsent(financialCalendar.getId(), financialCalendar)) {
			logger.debug("Added FinancialCalendar: {}", financialCalendar);
		}
	}

	protected void addHolidayDefinition(HolidayDefinition holidayDefinition) {
		if (null == holidayDefinitions.putIfAbsent(holidayDefinition.getId(), holidayDefinition)) {
			logger.debug("Added HolidayDefinition: {}", holidayDefinition);
		}
	}

	private void load(final InputStream definitionXmlStream) {
		// Copy and close the given input stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IOUtils.copy(definitionXmlStream, baos);
		} catch (IOException ioe) {
			throw new DefinitionSourceException("Cannot read definition XML input stream", ioe);
		}
		IOUtils.closeQuietly(definitionXmlStream);
		
		// Create 2 copies of XML input: 1 for validation, 1 for parse
		InputStream validatorIn = new ByteArrayInputStream(baos.toByteArray()),
				    parserIn    = new ByteArrayInputStream(baos.toByteArray());
		
		// Validate XML
		validateDefinitionXml(validatorIn);
		
		// Parse XML
		parseDefinitionXml(parserIn);
	}

	private boolean isContainerElement(StackElement curEl) {
		return (Constants.ELEMENT_DEFINITIONS.equals(curEl.getElementName()) ||
				Constants.ELEMENT_CENTRALBANKS.equals(curEl.getElementName()) ||
				Constants.ELEMENT_HOLIDAYS.equals(curEl.getElementName()) ||
				Constants.ELEMENT_CALENDARS.equals(curEl.getElementName()));
	}

	private boolean isEntityElement(StackElement curEl) {
		return ((Constants.ELEMENT_CENTRALBANK.equals(curEl.getElementName()) ||
				 Constants.ELEMENT_HOLIDAY.equals(curEl.getElementName()) ||
				 Constants.ELEMENT_CALENDAR.equals(curEl.getElementName())) &&
				(null != curEl.getElementId()));
	}

	private boolean isEntityReference(StackElement curEl) {
		return ((Constants.ELEMENT_CENTRALBANK.equals(curEl.getElementName()) ||
				 Constants.ELEMENT_HOLIDAY.equals(curEl.getElementName())) &&
				(null != curEl.getElementRefId()));
	}

	private void putEntityConstructorArg(String elLocalName, String strValue, Map<String, Object> constructorArgMap) {
		Object argValue = null;
		if (Constants.ELEMENT_OBSERVANCE.equals(elLocalName)) {
			argValue = HolidayType.valueOf(strValue);
		} else if (Constants.ELEMENT_CURRENCY.equals(elLocalName)) {
			argValue = Currency.getInstance(strValue);
		} else {
			argValue = strValue;
		}
		constructorArgMap.put(elLocalName, argValue);
		logger.debug("Added constructor arg: '{}' = {}", elLocalName, argValue);
	}

	@SuppressWarnings("unchecked")
	private void putEntityConstructorArgByRef(String elLocalName, String refId, Map<String, Object> constructorArgMap) {
		if (Constants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			CentralBank cb = getCentralBank(refId);
			constructorArgMap.put(elLocalName, cb);
			logger.debug("Put referenced CentralBank in constructor args: {}", cb);
		} else if (Constants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			HolidayDefinition hd = getHolidayDefinition(refId);
			Set<HolidayDefinition> holidays = (Set<HolidayDefinition>)constructorArgMap.get(Constants.ELEMENT_HOLIDAYS);
			if (null == holidays) {
				holidays = new HashSet<HolidayDefinition>();
			}
			holidays.add(hd);
			constructorArgMap.put(Constants.ELEMENT_HOLIDAYS, holidays);
			logger.debug("Added referenced HolidayDefinition to constructor args: {}", hd);
		}
	}

	@SuppressWarnings("unchecked")
	private void addEntity(String elLocalName, Map<String, Object> constructorArgMap) {
		final Object[] args;
		final Class<?>[] argTypes;
		if (Constants.ELEMENT_CENTRALBANK.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(Constants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(Constants.ELEMENT_NAME),
				(String)constructorArgMap.get(Constants.ELEMENT_COUNTRY),
				(Currency)constructorArgMap.get(Constants.ELEMENT_CURRENCY)
			};
			argTypes = new Class<?>[]{
				String.class,
				String.class,
				String.class,
				Currency.class
			};
			CentralBank cb = (CentralBank)constructDefinitionEntity(CentralBank.class, args, argTypes);
			addCentralBank(cb);
		} else if (Constants.ELEMENT_HOLIDAY.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(Constants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(Constants.ELEMENT_NAME),
				(String)constructorArgMap.get(Constants.ELEMENT_DESCRIPTION),
				(HolidayType)constructorArgMap.get(Constants.ELEMENT_OBSERVANCE),
				(String)constructorArgMap.get(Constants.ELEMENT_EXPRESSION),
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
		} else if (Constants.ELEMENT_CALENDAR.equals(elLocalName)) {
			args = new Object[] {
				(String)constructorArgMap.get(Constants.ATTRIBUTE_ID),
				(String)constructorArgMap.get(Constants.ELEMENT_DESCRIPTION),
				(CentralBank)constructorArgMap.get(Constants.ELEMENT_CENTRALBANK),
				(Set<HolidayDefinition>)constructorArgMap.get(Constants.ELEMENT_HOLIDAYS)
			};
			argTypes = new Class<?>[] {
				String.class,
				String.class,
				CentralBank.class,
				Set.class
			};
			FinancialCalendar fc = (FinancialCalendar)constructDefinitionEntity(FinancialCalendar.class, args, argTypes);
			addFinancialCalendar(fc);
		}
	}

	private Object constructDefinitionEntity(Class<?> cls, Object[] args, Class<?>[] argTypes) {
		try {
			return ConstructorUtils.invokeConstructor(cls, args, argTypes);
		} catch (Exception e) {
			throw new DefinitionSourceException("Cannot construct configuration entity of type: " + cls.getName(), e);
		}
	}

}
