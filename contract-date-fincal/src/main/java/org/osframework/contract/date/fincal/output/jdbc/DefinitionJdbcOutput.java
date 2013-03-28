/*
 * File: DefinitionJdbcOutput.java
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
import org.osframework.contract.date.fincal.definition.CentralBank;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.output.DefinitionOutput;

/**
 * Provides storage of <tt>FinancialCalendar</tt> definitions to a JDBC
 * <tt>DataSource</tt>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class DefinitionJdbcOutput extends AbstractJdbcOutput<FinancialCalendar>
	implements DefinitionOutput<DataSource, SQLException> {

	protected JdbcOutputHandler<CentralBank> centralBankHandler = null;
	protected JdbcOutputHandler<FinancialCalendar> financialCalendarHandler = null;
	protected JdbcOutputHandler<HolidayDefinition> holidayDefinitionHandler = null;

	/**
	 * 
	 */
	public DefinitionJdbcOutput() {
		super();
	}

	/**
	 * @param dataSource
	 */
	public DefinitionJdbcOutput(final DataSource dataSource,
			                    final JdbcOutputHandler<CentralBank> centralBankHandler,
			                    final JdbcOutputHandler<FinancialCalendar> financialCalendarHandler,
			                    final JdbcOutputHandler<HolidayDefinition> holidayDefinitionHandler) {
		super(dataSource);
		setCentralBankHandler(centralBankHandler);
		setFinancialCalendarHandler(financialCalendarHandler);
		setHolidayDefinitionHandler(holidayDefinitionHandler);
	}

	/**
	 * @param centralBankHandler the centralBankHandler to set
	 */
	public void setCentralBankHandler(JdbcOutputHandler<CentralBank> centralBankHandler) {
		Validate.notNull(centralBankHandler, "Property 'centralBankHandler' cannot be set to null");
		this.centralBankHandler = centralBankHandler;
		logger.debug("centralBankHandler set to: {}", centralBankHandler.getClass().getName());
	}

	/**
	 * @param financialCalendarHandler the financialCalendarHandler to set
	 */
	public void setFinancialCalendarHandler(JdbcOutputHandler<FinancialCalendar> financialCalendarHandler) {
		Validate.notNull(financialCalendarHandler, "Property 'financialCalendarHandler' cannot be set to null");
		this.financialCalendarHandler = financialCalendarHandler;
	}

	/**
	 * @param holidayDefinitionHandler the holidayDefinitionHandler to set
	 */
	public void setHolidayDefinitionHandler(JdbcOutputHandler<HolidayDefinition> holidayDefinitionHandler) {
		Validate.notNull(holidayDefinitionHandler, "Property 'holidayDefinitionHandler' cannot be set to null");
		this.holidayDefinitionHandler = holidayDefinitionHandler;
	}

	public void store(FinancialCalendar... calendars) throws SQLException {
		Connection connection = getConnection();
		try {
			for (FinancialCalendar fc : calendars) {
				centralBankHandler.storeInConnection(connection, fc.getCentralBank());
				for (HolidayDefinition hd : fc) {
					holidayDefinitionHandler.storeInConnection(connection, hd);
				}
				financialCalendarHandler.storeInConnection(connection, fc);
				logger.debug("Stored financial calendar '{}'", fc.getId());
			}
			connection.commit();
			logger.info("Stored {} total financial calendar definitions", Integer.valueOf(calendars.length));
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
