/*
 * File: JdbcOutputTransactionWorker.java
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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Defines behavior of objects which act within an open JDBC transaction to
 * store financial calendar-related objects to a database. Objects which
 * implement this interface or one of its extensions are employed as delegates
 * that perform the actual database-specific persistence.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 * @see AbstractJdbcOutput
 */
public interface JdbcOutputTransactionWorker<M> {

	/**
	 * Store the specified objects within a transaction opened on the given
	 * connection.
	 * 
	 * @param connection JDBC connection on which a transaction is open
	 * @param m object(s) to be stored
	 * @throws SQLException if persistence fails for any reason
	 */
	public void storeInTransaction(Connection connection, M... m) throws SQLException;

}
