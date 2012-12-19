/*
 * File: USFederalReserveDecorator.java
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
package org.osframework.contract.date.fincal.expression.centralbank;

import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.expression.HolidayExpressionDecorator;
import org.osframework.contract.date.fincal.model.CentralBank;

/**
 * Abstract superclass of decorators that provide <code>CentralBank</code>
 * policy adjustments to produced holiday dates.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class CentralBankDecorator extends HolidayExpressionDecorator {

	/**
	 * Constructor.
	 * 
	 * @param decoratedExpression holiday expression to be decorated
	 */
	public CentralBankDecorator(HolidayExpression decoratedExpression) {
		super(decoratedExpression);
	}

	/**
	 * Determine if this decorator supports the given central bank.
	 * 
	 * @param cb central bank to be identified
	 * @return <code>true</code> if central bank is supported,
	 *         <code>false</code> otherwise
	 */
	public abstract boolean supports(CentralBank cb);

}
