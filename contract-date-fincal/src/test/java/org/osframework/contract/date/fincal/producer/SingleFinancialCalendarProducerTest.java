/*
 * File: SingleFinancialCalendarProducerTest.java
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
package org.osframework.contract.date.fincal.producer;

import static org.testng.Assert.*;
import static org.osframework.contract.date.fincal.ObjectMother.*;

import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>SingleFinancialCalendarProducer</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SingleFinancialCalendarProducerTest {

	@Test(groups="producer",
		  dependsOnGroups={"model","centralbank"},
		  expectedExceptions=IllegalArgumentException.class)
	public void testConstructorNullFinancialCalendar() {
		new SingleFinancialCalendarProducer(null, false);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="producer",
		  dependsOnGroups={"model","centralbank"},
		  dataProvider="dp")
	public void testProduce(FinancialCalendar fc, boolean weekends, int year, int expectedCount) {
		SingleFinancialCalendarProducer sfcp = new SingleFinancialCalendarProducer(fc, weekends);
		Holiday[] results = sfcp.produce(year);
		assertEquals(results.length, expectedCount);
	}

	@DataProvider
	public Object[][] dp() {
		FinancialCalendar fc = createFinancialCalendar();
		Object[] set1 = new Object[] { fc, true, 2012, (fc.size() + 105) };
		Object[] set2 = new Object[] { fc, false, 2012, fc.size() };
		
		return new Object[][] {
			set1, set2
		};
	}

}
