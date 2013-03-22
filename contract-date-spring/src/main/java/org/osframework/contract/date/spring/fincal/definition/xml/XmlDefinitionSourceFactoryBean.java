/*
 * File: XmlDefinitionSourceFactoryBean.java
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
package org.osframework.contract.date.spring.fincal.definition.xml;

import java.io.File;
import java.net.URL;

import org.osframework.contract.date.fincal.definition.xml.XmlDefinitionSource;
import org.osframework.contract.date.spring.fincal.definition.AbstractDefinitionSourceFactoryBean;
import org.springframework.core.io.Resource;

/**
 * XmlDefinitionSourceFactoryBean description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class XmlDefinitionSourceFactoryBean extends
		AbstractDefinitionSourceFactoryBean<XmlDefinitionSource> {

	public XmlDefinitionSourceFactoryBean() {
		super(XmlDefinitionSource.class);
	}

	protected XmlDefinitionSource createInstance() throws Exception {
		XmlDefinitionSource definitionSource = null;
		Object s = getSource();
		// Cannot be null
		if (null == s) {
			throw new IllegalStateException("Missing required 'source' property");
		} else {
			if (s instanceof File) {
				definitionSource = new XmlDefinitionSource((File)s);
			} else if (s instanceof Resource) {
				definitionSource = new XmlDefinitionSource(((Resource)s).getURL());
			} else if (s instanceof URL) {
				definitionSource = new XmlDefinitionSource((URL)s);
			}
		}
		if (null == definitionSource) {
			throw new IllegalArgumentException("Invalid 'source' property value type " + s.getClass().getName());
		}
		return definitionSource;
	}

}
