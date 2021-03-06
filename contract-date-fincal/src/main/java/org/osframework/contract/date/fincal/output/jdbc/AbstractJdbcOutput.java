/*
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
package org.osframework.contract.date.fincal.output.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osframework.contract.date.fincal.output.AbstractOutput;

/**
 * Abstract superclass of objects which store definition or generated
 * <tt>Holiday</tt> objects to a JDBC <tt>DataSource</tt>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractJdbcOutput<M> extends AbstractOutput<M, DataSource, SQLException> {

	protected DataSource dataSource;
	private volatile boolean closed;

	public AbstractJdbcOutput() {
		super();
		this.closed = false;
	}

	public AbstractJdbcOutput(final DataSource dataSource) {
		this();
		this.dataSource = dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void close() throws SQLException {
		closed = true;
	}

	protected Connection getConnection() throws SQLException {
		if (closed) {
			throw new SQLException("Output has already been closed");
		}
		Connection c = dataSource.getConnection();
		c.setAutoCommit(false);
		return c;
	}

}
