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
package org.osframework.contract.date.fincal.config.xml;

import javax.xml.XMLConstants;
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
	public static final String CONTRACT_DATE_FINCAL_NS_URI = "http://osframework.org/schema/contract-date/definitions";

	/**
	 * Filename of financial calendar definition XML Schema document.
	 */
	public static final String XSD_FILENAME = "definitions.xsd";

	/**
	 * Resolvable classpath of XML Schema document resource.
	 */
	public static final String XSD_RESOURCE = XmlConstants.class.getPackage().getName().replace('.','/') + "/" + XSD_FILENAME;

	/**
	 * Default XML character encoding.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Local (unqualified) name of 'calendar' element.
	 */
	public static final String ELEMENT_CALENDAR     = "calendar";

	/**
	 * Local (unqualified) name of 'calendars' element.
	 */
	public static final String ELEMENT_CALENDARS    = "calendars";

	/**
	 * Local (unqualified) name of 'centralBank' element.
	 */
	public static final String ELEMENT_CENTRALBANK  = "centralBank";

	/**
	 * Local (unqualified) name of 'centralBanks' element.
	 */
	public static final String ELEMENT_CENTRALBANKS = "centralBanks";

	/**
	 * Local (unqualified) name of 'country' element.
	 */
	public static final String ELEMENT_COUNTRY      = "country";

	/**
	 * Local (unqualified) name of 'currency' element.
	 */
	public static final String ELEMENT_CURRENCY     = "currency";

	/**
	 * Local (unqualified) name of 'definitions' top-level element. This is
	 * usually the document root element.
	 */
	public static final String ELEMENT_DEFINITIONS  = "definitions";

	/**
	 * Local (unqualified) name of 'description' element.
	 */
	public static final String ELEMENT_DESCRIPTION  = "description";

	/**
	 * Local (unqualified) name of 'expression' element.
	 */
	public static final String ELEMENT_EXPRESSION   = "expression";

	/**
	 * Local (unqualified) name of 'holiday' element.
	 */
	public static final String ELEMENT_HOLIDAY      = "holiday";

	/**
	 * Local (unqualified) name of 'holidays' element.
	 */
	public static final String ELEMENT_HOLIDAYS     = "holidays";

	/**
	 * Local (unqualified) name of 'name' element.
	 */
	public static final String ELEMENT_NAME         = "name";

	/**
	 * Local (unqualified) name of 'observance' element.
	 */
	public static final String ELEMENT_OBSERVANCE   = "observance";

	/**
	 * Local (unqualified) name of 'id' attribute.
	 */
	public static final String ATTRIBUTE_ID         = "id";

	/**
	 * Local (unqualified) name of 'refid' attribute.
	 */
	public static final String ATTRIBUTE_REFID      = "refid";

	/**
	 * @return shared instance of <code>Schema</code> object for
	 *         <code>definitions.xsd</code>.
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
		Schema s = null;
		try {
			s = sf.newSchema(this.getClass().getResource("/" + XSD_RESOURCE));
		} catch (SAXException e) {
			s = null;
		}
		this.schema = s;
	}

	private static class XmlConstantsSingleton {
		static final XmlConstants INSTANCE = new XmlConstants();
	}

}
