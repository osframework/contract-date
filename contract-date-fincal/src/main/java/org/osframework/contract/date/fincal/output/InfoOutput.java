/*
 * File: InfoOutput.java
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
package org.osframework.contract.date.fincal.output;

import org.osframework.contract.date.fincal.definition.FinancialCalendar;

/**
 * Defines behavior of objects which store <code>FinancialCalendar</code>
 * objects to an <i>info</i> manifest location, for use in combination with a
 * corresponding <i>holiday data</i> location.
 *
 * @param <T> Output destination type
 * @param <E> Base exception class thrown by operations on output
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface InfoOutput<T, E extends Exception>
	extends Output<FinancialCalendar, T, E> {

	static final String[] COLUMNS = {
		"CODE","CENTER","COUNTRY","CURRENCY",
		"TYPE","ISOCOUNTRY","DEFINE1","DEFINE2"
	};

	static final String DEFAULT_DEFINE1 = "Banks will be closed for the entire day for the purpose of making payments.";

}
