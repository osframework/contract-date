/*
 * File: XmlConstants.java
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

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Utility class to contain basic XML values as constants.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class XmlConstants {

	/**
	 * The financial calendar definition namespace URI.
	 */
	public static final String CONTRACT_DATE_FINCAL_NS_URI = "http://osframework.org/schema/contract-date-fincal";

	/**
	 * Filename of financial calendar definition XML Schema document.
	 */
	public static final String XSD_FILENAME = "definitions.xsd";

	/**
	 * Resolvable classpath of XML Schema document resource.
	 */
	public static final String XSD_RESOURCE = "/" + XSD_FILENAME;

	/**
	 * Default XML character encoding.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Local (unqualified) name of 'calendar' element.
	 */
	public static final String ELEMENT_CALENDAR = "calendar";

	/**
	 * Qualified name of 'calendar' element.
	 */
	public static final QName QNAME_CALENDAR = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_CALENDAR);

	/**
	 * Local (unqualified) name of 'calendars' element.
	 */
	public static final String ELEMENT_CALENDARS = "calendars";

	/**
	 * Qualified name of 'calendars' element.
	 */
	public static final QName QNAME_CALENDARS = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_CALENDARS);

	/**
	 * Local (unqualified) name of 'centralBank' element.
	 */
	public static final String ELEMENT_CENTRALBANK  = "centralBank";

	/**
	 * Qualified name of 'centralBank' element.
	 */
	public static final QName QNAME_CENTRALBANK = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_CENTRALBANK);

	/**
	 * Local (unqualified) name of 'centralBanks' element.
	 */
	public static final String ELEMENT_CENTRALBANKS = "centralBanks";

	/**
	 * Qualified name of 'centralBanks' element.
	 */
	public static final QName QNAME_CENTRALBANKS = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_CENTRALBANKS);

	/**
	 * Local (unqualified) name of 'country' element.
	 */
	public static final String ELEMENT_COUNTRY = "country";

	/**
	 * Qualified name of 'currency' element.
	 */
	public static final QName QNAME_COUNTRY = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_COUNTRY);

	/**
	 * Local (unqualified) name of 'currency' element.
	 */
	public static final String ELEMENT_CURRENCY = "currency";

	/**
	 * Qualified name of 'currency' element.
	 */
	public static final QName QNAME_CURRENCY = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_CURRENCY);

	/**
	 * Local (unqualified) name of 'definitions' top-level element. This is the
	 * document root element.
	 */
	public static final String ELEMENT_DEFINITIONS  = "definitions";

	/**
	 * Qualified name of 'definitions' top-level element. This is the document
	 * root element.
	 */
	public static final QName QNAME_DEFINITIONS = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_DEFINITIONS);

	/**
	 * Local (unqualified) name of 'description' element.
	 */
	public static final String ELEMENT_DESCRIPTION  = "description";

	/**
	 * Qualified name of 'description' element.
	 */
	public static final QName QNAME_DESCRIPTION = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_DESCRIPTION);

	/**
	 * Local (unqualified) name of 'expression' element.
	 */
	public static final String ELEMENT_EXPRESSION  = "expression";

	/**
	 * Qualified name of 'expression' element.
	 */
	public static final QName QNAME_EXPRESSION = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_EXPRESSION);

	/**
	 * Local (unqualified) name of 'holidayDefinition' element.
	 */
	public static final String ELEMENT_HOLIDAYDEFINITION = "holidayDefinition";

	/**
	 * Qualified named of 'holidayDefinition' element.
	 */
	public static final QName QNAME_HOLIDAYDEFINITION = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_HOLIDAYDEFINITION);

	/**
	 * Local (unqualified) name of 'holidayDefinitions' element.
	 */
	public static final String ELEMENT_HOLIDAYDEFINITIONS = "holidayDefinitions";

	/**
	 * Qualified name of 'holidayDefinitions' element.
	 */
	public static final QName QNAME_HOLIDAYDEFINITIONS = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_HOLIDAYDEFINITIONS);

	/**
	 * Local (unqualified) name of 'name' element.
	 */
	public static final String ELEMENT_NAME = "name";

	/**
	 * Qualified name of 'name' element.
	 */
	public static final QName QNAME_NAME = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_NAME);

	/**
	 * Local (unqualified) name of 'observance' element.
	 */
	public static final String ELEMENT_OBSERVANCE = "observance";

	/**
	 * Qualified name of 'observance' element.
	 */
	public static final QName QNAME_OBSERVANCE = new QName(CONTRACT_DATE_FINCAL_NS_URI, ELEMENT_OBSERVANCE);

	/**
	 * Local (unqualified) name of 'id' attribute.
	 */
	public static final String ATTRIBUTE_ID = "id";

	/**
	 * @return shared instance of <code>Schema</code> object for
	 *         <tt>definitions.xsd</tt>.
	 */
	public static Schema getSchema() {
		return XmlConstantsSingleton.INSTANCE.getSingletonSchema();
	}

	Schema getSingletonSchema() {
		return this.schema;
	}

	private final Schema schema;

	/**
     * Private constructor to prevent instantiation.
     */
	private XmlConstants() {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			this.schema = sf.newSchema(this.getClass().getResource(XSD_RESOURCE));
		} catch (SAXException se) {
			throw new RuntimeException("Failed to created singleton Schema object", se);
		}
	}

	private static class XmlConstantsSingleton {
		static final XmlConstants INSTANCE = new XmlConstants();
	}

}
