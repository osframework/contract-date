/*
 * File: SingleYearProducerTest.java
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

import static org.osframework.contract.date.fincal.ObjectMother.createFinancialCalendar;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>SingleYearProducer</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SingleYearProducerTest {

	@Test(groups="producer",
		  dependsOnGroups={"model","centralbank"},
		  expectedExceptions=IllegalArgumentException.class)
	public void testConstructorNullFinancialCalendar() {
		new SingleYearProducer(null);
		fail("Expected IllegalArgumentException to be thrown");
	}

	@Test(groups="producer",
		  dependsOnGroups={"model","centralbank"},
		  dataProvider="weekends")
	public void testIncludesWeekends(int year, boolean expected) {
		SingleYearProducer syp = new SingleYearProducer(year, expected);
		assertEquals(syp.includesWeekends(), expected);
	}

	@Test(groups="producer",
		  dependsOnGroups={"model","centralbank"},
		  dataProvider="dp")
	public void testProduce(int year, boolean weekends, FinancialCalendar fc, int expectedCount) {
		SingleYearProducer syp = new SingleYearProducer(year, weekends);
		Holiday[] results = syp.produce(fc);
		assertEquals(results.length, expectedCount);
	}

	@DataProvider
	public Object[][] dp() {
		FinancialCalendar fc = createFinancialCalendar();
		Object[] set1 = new Object[] {2012, true,  fc, (fc.size() + 105) };
		Object[] set2 = new Object[] {2012, false, fc, fc.size() };
		
		return new Object[][] {
			set1, set2
		};
	}

	@DataProvider
	public Object[][] weekends() {
		Object[] set1 = new Object[] { 2012, true };
		Object[] set2 = new Object[] { 2012, false };
		
		return new Object[][] {
			set1, set2
		};
	}

}
