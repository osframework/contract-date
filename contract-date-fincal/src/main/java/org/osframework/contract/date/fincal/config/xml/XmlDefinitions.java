/*
 * File: XmlDefinitions.java
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.osframework.contract.date.fincal.config.Definitions;
import org.xml.sax.SAXException;

/**
 * Financial calendar definitions loaded from an XML document.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class XmlDefinitions extends Definitions {

	private static final String XSD_DIR = XmlDefinitions.class.getPackage().getName().replace('.', '/');
	public static final String XSD_PATH = "/" + XSD_DIR + "/contract-date-fincal.xsd";

	private final Reader reader;

	public XmlDefinitions(String xmlDefinitionLocation) throws Exception {
		this(new File(xmlDefinitionLocation));
	}

	public XmlDefinitions(File xmlDefinitionLocation) throws Exception {
		this(new FileInputStream(xmlDefinitionLocation));
	}

	public XmlDefinitions(URL xmlDefinitionLocation) throws Exception {
		this(xmlDefinitionLocation.openStream());
	}

	public XmlDefinitions(InputStream xmlDefinitionStream) throws Exception {
		this.reader = new BufferedReader(new InputStreamReader(xmlDefinitionStream));
		this.load();
	}

	@Override
	protected void load() throws Exception {
		try {
			XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(this.reader);
			URL xsdUrl = this.getClass().getResource(XSD_PATH);
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema s = sf.newSchema(xsdUrl);
			
			Validator v = s.newValidator();
			v.validate(new StAXSource(xsr));
			
			// Definitions valid at this point
		} catch (XMLStreamException xse) {
			throw new IOException(xse);
		} catch (SAXException se) {
			throw new IOException(se);
		}
	}

}
