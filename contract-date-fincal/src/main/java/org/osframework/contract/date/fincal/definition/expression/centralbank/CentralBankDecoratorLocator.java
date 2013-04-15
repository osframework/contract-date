/*
 * File: CentralBankDecoratorLocator.java
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
package org.osframework.contract.date.fincal.definition.expression.centralbank;

import java.lang.reflect.Constructor;
import java.util.Iterator;

import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.HolidayExpression;
import org.osframework.util.ServiceClassLoader;

/**
 * Provides dynamic, central bank-specific decoration of a
 * <code>HolidayExpression</code> object via lazy provider
 * location.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public final class CentralBankDecoratorLocator {

	/**
	 * Private constructor - prevents instantiation.
	 */
	private CentralBankDecoratorLocator() {}

	public static HolidayExpression decorate(final HolidayExpression targetExpr, CentralBank bank) {
		HolidayExpression decoratedExpr = null;
		ServiceClassLoader<CentralBankDecorator> scl = ServiceClassLoader.load(CentralBankDecorator.class);
		Iterator<Class<? extends CentralBankDecorator>> it = scl.iterator();
		while (it.hasNext()) {
			Class<? extends CentralBankDecorator> cbdClass = it.next();
			try {
				Constructor<? extends CentralBankDecorator> c = cbdClass.getConstructor(HolidayExpression.class);
				CentralBankDecorator cbd = c.newInstance(targetExpr);
				if (cbd.supports(bank)) {
					decoratedExpr = cbd;
					break;
				}
			} catch (Exception e) {
				// FIXME Log the exception?
				continue;
			}
		}
		return (null != decoratedExpr) ? decoratedExpr : targetExpr;
	}

}
