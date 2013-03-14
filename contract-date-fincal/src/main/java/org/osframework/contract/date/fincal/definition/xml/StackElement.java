/*
 * File: StackElement.java
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
package org.osframework.contract.date.fincal.definition.xml;

import org.apache.commons.lang.Validate;

/**
 * Simple XML element reference for use in parser stacks. An instance of
 * this class must, at minimum, provide a local element name.
 * <p>
 * <code>StackElement</code> objects are immutable and thread-safe.
 * </p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class StackElement {

	private final String elementName;
	private final String elementId;
	private final String elementRefId;

	public StackElement(final String elementName,
			            final String elementId,
			            final String elementRefId) {
		Validate.notEmpty(elementName, "argument 'elementName' cannot be empty");
		this.elementName = elementName;
		this.elementId = elementId;
		this.elementRefId = elementRefId;
	}

	public String getElementName() {
		return elementName;
	}

	public String getElementId() {
		return elementId;
	}

	public String getElementRefId() {
		return elementRefId;
	}

}