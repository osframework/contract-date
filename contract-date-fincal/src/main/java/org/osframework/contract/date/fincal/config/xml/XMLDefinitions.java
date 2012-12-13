/*
 * File: XMLDefinitions.java
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
import java.io.InputStream;
import java.net.URL;
import java.util.Currency;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.NotImplementedException;
import org.osframework.contract.date.fincal.config.Definitions;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.osframework.contract.date.fincal.model.HolidayType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Provides financial calendar definitions from an XML document.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class XMLDefinitions extends Definitions<Document> implements Constants {

	private static final String XPATH_HOLIDAY = "//d:definitions/d:holidays/d:holiday";
	private static final String XPATH_HOLIDAY_REFID = "./d:holidays/d:holiday/@refid";
	private static final String XPATH_CENTRALBANK = "//d:definitions/d:centralBanks/d:centralBank";
	private static final String XPATH_CALENDAR = "//d:definitions/d:calendars/d:calendar";

	private XPathExpression holidayDefinitionExpr = null,
			                centralBankExpr = null,
			                financialCalendarExpr = null,
			                holidayRefidExpr = null;

	public XMLDefinitions(String xmlDefinitionLocation) throws Exception {
		this(new URL(xmlDefinitionLocation));
	}

	public XMLDefinitions(File xmlDefinitionLocation) throws Exception {
		this(new FileInputStream(xmlDefinitionLocation));
	}

	public XMLDefinitions(URL xmlDefinitionLocation) throws Exception {
		this(xmlDefinitionLocation.openStream());
	}

	public XMLDefinitions(final InputStream xmlDefinitionStream) throws Exception {
		super();
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(this.getClass().getResource("/" + XSD_RESOURCE));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setSchema(schema);
		dbf.setNamespaceAware(true);
		
		this.definitionSource = dbf.newDocumentBuilder().parse(xmlDefinitionStream);
		
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		xPath.setNamespaceContext(new NamespaceContext() {
			public Iterator<?> getPrefixes(String namespaceURI) {
				return null;
			}
			public String getPrefix(String namespaceURI) {
				return null;
			}
			public String getNamespaceURI(String prefix) {
				return ("d".equals(prefix)) ? CONTRACT_DATE_FINCAL_NS_URI : XMLConstants.NULL_NS_URI;
			}
		});
		this.holidayDefinitionExpr = xPath.compile(XPATH_HOLIDAY);
		this.centralBankExpr = xPath.compile(XPATH_CENTRALBANK);
		this.financialCalendarExpr = xPath.compile(XPATH_CALENDAR);
		this.holidayRefidExpr = xPath.compile(XPATH_HOLIDAY_REFID);
	}

	public XMLDefinitions(Definitions<?> originalDefinitions) {
		super(originalDefinitions);
	}

	@Override
	protected void doLoad() throws Exception {
		loadHolidayDefinitions();
		loadCentralBanks();
		loadFinancialCalendars();
	}

	@Override
	protected void doStore() throws Exception {
		throw new NotImplementedException(this.getClass());
	}

	void loadHolidayDefinitions() throws Exception {
		NodeList nodes = (NodeList)holidayDefinitionExpr.evaluate(definitionSource, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element hdEl = (Element)nodes.item(i);
			String id = hdEl.getAttribute(ATTRIBUTE_ID);
			String name = hdEl.getElementsByTagName(ELEMENT_NAME).item(0).getTextContent().trim();
			NodeList dEl = hdEl.getElementsByTagName(ELEMENT_DESCRIPTION);
			String description = (0 < dEl.getLength())
					              ? dEl.item(0).getTextContent().trim()
					              : null;
			String observance = hdEl.getElementsByTagName(ELEMENT_OBSERVANCE).item(0).getTextContent().trim();
			String expression = hdEl.getElementsByTagName(ELEMENT_EXPRESSION).item(0).getTextContent().trim();
			HolidayDefinition hd = new HolidayDefinition();
			hd.setId(id);
			hd.setName(name);
			hd.setDescription(description);
			hd.setObservance(HolidayType.valueOf(observance));
			hd.setExpression(expression);
			addHolidayDefinition(hd);
		}
	}

	void loadCentralBanks() throws Exception {
		NodeList nodes = (NodeList)centralBankExpr.evaluate(definitionSource, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element cbEl = (Element)nodes.item(i);
			String id = cbEl.getAttribute(ATTRIBUTE_ID);
			String name = cbEl.getElementsByTagName(ELEMENT_NAME).item(0).getTextContent().trim();
			String ccy = cbEl.getElementsByTagName(ELEMENT_CURRENCY).item(0).getTextContent().trim();
			CentralBank cb = new CentralBank();
			cb.setId(id);
			cb.setName(name);
			cb.setCurrency(Currency.getInstance(ccy));
			addCentralBank(cb);
		}
	}

	void loadFinancialCalendars() throws Exception {
		NodeList nodes = (NodeList)financialCalendarExpr.evaluate(definitionSource, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element fcEl = (Element)nodes.item(i);
			String id = fcEl.getAttribute(ATTRIBUTE_ID);
			NodeList dEl = fcEl.getElementsByTagName(ELEMENT_DESCRIPTION);
			String description = (0 < dEl.getLength())
		              ? dEl.item(0).getTextContent().trim()
		              : null;
		  	String cbId = ((Element)fcEl.getElementsByTagName(ELEMENT_CENTRALBANK).item(0)).getAttribute(ATTRIBUTE_REFID);
		  	CentralBank cb = getCentralBank(cbId);
		  	NodeList hdRefs = (NodeList)holidayRefidExpr.evaluate(fcEl, XPathConstants.NODESET);
		  	HolidayDefinition[] hdArray = new HolidayDefinition[hdRefs.getLength()];
		  	for (int j = 0; j < hdRefs.getLength(); j++) {
		  		String hdId = hdRefs.item(j).getNodeValue();
		  		hdArray[j] = getHolidayDefinition(hdId);
		  	}
		  	FinancialCalendar fc = new FinancialCalendar();
		  	fc.setId(id);
		  	fc.setDescription(description);
		  	fc.setCentralBank(cb);
		  	for (HolidayDefinition hd : hdArray) {
		  		fc.add(hd);
		  	}
		  	addFinancialCalendar(fc);
		}
	}

}
