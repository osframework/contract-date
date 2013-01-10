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

	public static final String ELEMENT_CALENDAR     = "calendar";
	public static final String ELEMENT_CALENDARS    = "calendars";
	public static final String ELEMENT_CENTRALBANK  = "centralBank";
	public static final String ELEMENT_CENTRALBANKS = "centralBanks";
	public static final String ELEMENT_COUNTRY      = "country";
	public static final String ELEMENT_CURRENCY     = "currency";
	public static final String ELEMENT_DEFINITIONS  = "definitions";
	public static final String ELEMENT_DESCRIPTION  = "description";
	public static final String ELEMENT_EXPRESSION   = "expression";
	public static final String ELEMENT_HOLIDAY      = "holiday";
	public static final String ELEMENT_HOLIDAYS     = "holidays";
	public static final String ELEMENT_NAME         = "name";
	public static final String ELEMENT_OBSERVANCE   = "observance";
	
	public static final String ATTRIBUTE_ID         = "id";
	public static final String ATTRIBUTE_REFID      = "refid";

	/**
     * Private constructor to prevent instantiation.
     */
	private XmlConstants() {}

}
