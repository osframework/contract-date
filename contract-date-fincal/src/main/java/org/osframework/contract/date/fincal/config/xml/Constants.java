/*
 * File: Constants.java
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
 * Constants description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface Constants {

	public static final String CONTRACT_DATE_FINCAL_NS_URI = "http://osframework.org/schema/contract-date/definitions";
	public static final String XSD_FILENAME = "definitions.xsd";
	public static final String XSD_RESOURCE = Constants.class.getPackage().getName().replace('.','/') + "/" + XSD_FILENAME;

	public static final String ELEMENT_CALENDAR = "calendar";
	public static final String ELEMENT_CENTRALBANK = "centralBank";
	public static final String ELEMENT_COUNTRY = "country";
	public static final String ELEMENT_CURRENCY = "currency";
	public static final String ELEMENT_DEFINITIONS = "definitions";
	public static final String ELEMENT_DESCRIPTION = "description";
	public static final String ELEMENT_EXPRESSION = "expression";
	public static final String ELEMENT_HOLIDAY = "holiday";
	public static final String ELEMENT_NAME = "name";
	public static final String ELEMENT_OBSERVANCE = "observance";
	
	public static final String ATTRIBUTE_ID = "id";
	public static final String ATTRIBUTE_REFID = "refid";

}
