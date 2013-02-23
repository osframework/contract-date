/*
 * File: BankOfEnglandDecorator.java
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

import java.util.Calendar;
import java.util.Date;

import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.model.CentralBank;

/**
 * Applies date adjustments per Bank of England bank holiday guidelines -
 * <blockquote>
 * If a bank holiday is on a weekend, a 'substitute' weekday becomes a bank
 * holiday, normally the following Monday.
 * </blockquote>
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see <a href="https://www.gov.uk/bank-holidays">UK bank holidays</a>
 */
public class BankOfEnglandDecorator extends CentralBankDecorator {

	public static final String CENTRAL_BANK = "BoE";

	/**
	 * Constructor.
	 * 
	 * @param decoratedExpression holiday expression to be decorated
	 */
	public BankOfEnglandDecorator(HolidayExpression decoratedExpression) {
		super(decoratedExpression);
	}

	public boolean supports(CentralBank cb) {
		return CENTRAL_BANK.equals(cb.getId());
	}

	@Override
	public Date evaluate(int year) {
		Date raw = super.evaluate(year);
		if (null == raw) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(raw);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		switch (weekday) {
		case Calendar.SATURDAY:
			c.add(Calendar.DAY_OF_MONTH, 2);
			break;
		case Calendar.SUNDAY:
			c.add(Calendar.DAY_OF_MONTH, 1);
			break;
		default:
			break;
		}
		return c.getTime();
	}

}
