/*
 * File: FinancialCalendarTransactionWorker.java
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
package org.osframework.contract.date.fincal.output.jdbc;

import org.osframework.contract.date.fincal.definition.FinancialCalendar;

/**
 * Provides in-transaction JDBC persistence behavior for storage of
 * <tt>FinancialCalendar</tt> definition object graphs. Objects which implement
 * this interface will store, to one or more tables:
 * <ul>
 * 	<li>A <tt>FinancialCalendar</tt> object, and
 * 		<ul>
 * 			<li>its associated <tt>CentralBank</tt> object</li>
 * 			<li>its associated <tt>HolidayDefinition</tt> objects</li>
 * 		</ul></li>
 * 	</li>
 * </ul>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface FinancialCalendarTransactionWorker extends
	JdbcOutputTransactionWorker<FinancialCalendar> {

}
