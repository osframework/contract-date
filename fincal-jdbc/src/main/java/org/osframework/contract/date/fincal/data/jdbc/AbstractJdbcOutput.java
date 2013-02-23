/**
 * File: AbstractJdbcOutput.java
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
package org.osframework.contract.date.fincal.data.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osframework.contract.date.fincal.data.AbstractOutput;

/**
 * AbstractJdbcOutput description here.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractJdbcOutput<M> extends AbstractOutput<M, DataSource, SQLException> {

	protected final DataSource dataSource;
	protected final JdbcOutputDelegate<M> delegate;

	private volatile boolean closed;

	public AbstractJdbcOutput(final DataSource dataSource,
			                  final JdbcOutputDelegate<M> delegate) throws SQLException {
		super();
		this.dataSource = dataSource;
		this.delegate = delegate;
		this.closed = false;
	}

	@Override
	public void store(M... m) throws SQLException {
		if (closed) {
			throw new SQLException("Cannot perform storage after invocation of close method");
		}
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			delegate.storeInDatabase(connection, m);
		} catch (SQLException se) {
			if (null != connection) {
				connection.rollback();
			}
			throw se;
		} finally {
			if (null != connection) {
				connection.close();
			}
		}
	}

	public void close() throws SQLException {
		closed = true;	
	}

}
