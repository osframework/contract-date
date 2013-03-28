/*
 * File: HolidayJdbcOutput.java
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

import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.holiday.Holiday;
import org.osframework.contract.date.fincal.output.HolidayOutput;

/**
 * Provides storage of <tt>Holiday</tt> objects to a JDBC <tt>DataSource</tt>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayJdbcOutput extends AbstractJdbcOutput<Holiday>
	implements HolidayOutput<DataSource, SQLException> {

	protected JdbcOutputHandler<Holiday> holidayHandler = null;

	public HolidayJdbcOutput() {
		super();
	}

	public HolidayJdbcOutput(final DataSource dataSource,
			                 final JdbcOutputHandler<Holiday> holidayHandler) {
		super(dataSource);
		
	}

	/**
	 * @param holidayHandler the holidayHandler to set
	 */
	public void setHolidayHandler(JdbcOutputHandler<Holiday> holidayHandler) {
		Validate.notNull(holidayHandler, "Property 'holidayHandler' cannot be set to null");
		this.holidayHandler = holidayHandler;
	}

	public void store(Holiday... holidays) throws SQLException {
		Connection connection = getConnection();
		try {
			for (Holiday h : holidays) {
				holidayHandler.storeInConnection(connection, h);
				logger.debug("Stored holiday '{}'", h.getId());
			}
			connection.commit();
			logger.info("Stored {} total holidays", Integer.valueOf(holidays.length));
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

}
