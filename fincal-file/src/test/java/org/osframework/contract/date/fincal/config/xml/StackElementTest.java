/*
 * File: StackElementTest.java
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
package org.osframework.contract.date.fincal.config.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.osframework.contract.date.fincal.definition.xml.StackElement;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>StackElement</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class StackElementTest {

	@Test(groups = {"config"},
		  dependsOnGroups = {"model"})
	public void testConstructorTypicalCase() {
		StackElement el = new StackElement("calendar", "NYB", null);
		assertEquals(el.getElementName(), "calendar");
		assertEquals(el.getElementId(), "NYB");
		assertNull(el.getElementRefId());
	}

	@Test(groups = {"config"},
		  dependsOnGroups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorNullElementName() {
		new StackElement(null, "NYB", null);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups = {"config"},
		  dependsOnGroups = {"model"},
		  expectedExceptions = IllegalArgumentException.class)
	public void testConstructorBlankElementName() {
		new StackElement("", "NYB", null);
		fail("Expected IllegalArgumentException to be thrown");
	}

}
