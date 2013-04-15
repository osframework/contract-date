/*
 * File: CentralBankDecoratorLocatorTest.java
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
package org.osframework.contract.date.fincal.definition.expression.centralbank;

import static org.osframework.contract.date.fincal.ObjectMother.CENTRAL_BANK_ID_BOE;
import static org.osframework.contract.date.fincal.ObjectMother.CENTRAL_BANK_ID_CBOA;
import static org.osframework.contract.date.fincal.ObjectMother.CENTRAL_BANK_ID_USFR;
import static org.osframework.contract.date.fincal.ObjectMother.HOLIDAY_DEF_ID_NEW_YEARS_DAY;
import static org.osframework.contract.date.fincal.ObjectMother.createCentralBank;
import static org.osframework.contract.date.fincal.ObjectMother.createHolidayDefinition;
import static org.testng.Assert.assertEquals;

import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.definition.HolidayExpression;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for <code>CentralBankDecoratorLocator</code>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CentralBankDecoratorLocatorTest {

	@Test(groups = "centralbank",
		  dependsOnGroups = "expression",
		  dataProvider = "dp")
	public void testDecorate(HolidayExpression expr, CentralBank cb, boolean expected) {
		HolidayExpression result = CentralBankDecoratorLocator.decorate(expr, cb);
		boolean actual = (result instanceof CentralBankDecorator);
		assertEquals(actual, expected);
	}

	@DataProvider
	public Object[][] dp() {
		HolidayDefinition nydDef = createHolidayDefinition(HOLIDAY_DEF_ID_NEW_YEARS_DAY);
		Object[] set1 = new Object[] { nydDef, createCentralBank(CENTRAL_BANK_ID_BOE), true };
		Object[] set2 = new Object[] { nydDef, createCentralBank(CENTRAL_BANK_ID_USFR), true };
		Object[] set3 = new Object[] { nydDef, createCentralBank(CENTRAL_BANK_ID_CBOA), false };
		
		return new Object[][] {
			set1, set2, set3
		};
	}

}
