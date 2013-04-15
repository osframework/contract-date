/*
 * File: HolidayExpressionDecorator.java
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
package org.osframework.contract.date.fincal.definition.expression;

import java.util.Date;

import org.osframework.contract.date.fincal.definition.HolidayExpression;

/**
 * Abstract superclass of objects that decorate an underlying
 * <code>HolidayExpression</code> instance's {@link #evaluate(int)} method with
 * some additional runtime logic.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class HolidayExpressionDecorator implements HolidayExpression {

	protected final HolidayExpression decoratedExpression;

	/**
	 * Constructor - accepts target expression object to be decorated.
	 * 
	 * @param decoratedExpression target expression object to be decorated
	 */
	public HolidayExpressionDecorator(final HolidayExpression decoratedExpression) {
		if (null == decoratedExpression) {
			throw new IllegalArgumentException("argument 'decoratedExpression' cannot be null");
		}
		this.decoratedExpression = decoratedExpression;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		return decoratedExpression.evaluate(year);
	}

}
