/*
 * File: BerkeleyDBDefinitions.java
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
package org.osframework.contract.date.fincal.config.bdb;

import java.io.File;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.osframework.contract.date.fincal.config.Definitions;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;
import org.osframework.contract.date.fincal.model.HolidayType;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * Provides financial calendar definitions from a Berkeley DB database.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class BerkeleyDBDefinitions extends Definitions<Environment> {

	public static final String HOME_DIRECTORY_DEFAULT = System.getProperty("user.home") + "/.contract-date-fincal";

	private Database centralBankDb, holidayDefinitionDb, financialCalendarDb;

	public BerkeleyDBDefinitions() throws Exception {
		this(HOME_DIRECTORY_DEFAULT);
	}

	public BerkeleyDBDefinitions(String dbHomeDirectory) throws Exception {
		this(new File(dbHomeDirectory));
	}

	public BerkeleyDBDefinitions(File dbHomeDirectory) throws Exception {
		super();
		if (null == dbHomeDirectory) {
			throw new IllegalArgumentException("dbHomeDirectory argument cannot be null");
		}
		if (!dbHomeDirectory.exists()) {
			dbHomeDirectory.mkdirs();
		}
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setReadOnly(false);
		this.definitionSource = new Environment(dbHomeDirectory, envConfig);
		
		
	}

	public BerkeleyDBDefinitions(Definitions<?> original) {
		super(original);
	}

	@Override
	protected void doLoad() throws Exception {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(false);
		try {
			centralBankDb = definitionSource.openDatabase(null, "centralBank", dbConfig);
			holidayDefinitionDb = definitionSource.openDatabase(null, "holidayDefinition", dbConfig);
			financialCalendarDb = definitionSource.openDatabase(null, "financialCalendar", dbConfig);
			loadCentralBanks();
			loadHolidayDefinitions();
			loadFinancialCalendars();
		} finally {
			if (null != centralBankDb) {
				centralBankDb.close();
			}
			if (null != holidayDefinitionDb) {
				holidayDefinitionDb.close();
			}
			if (null != financialCalendarDb) {
				financialCalendarDb.close();
			}
			definitionSource.close();
		}
	}

	@Override
	protected void doStore() throws Exception {
		throw new NotImplementedException(this.getClass());
	}

	void loadCentralBanks() throws DatabaseException {
		DatabaseEntry key = new DatabaseEntry(), data = new DatabaseEntry();
		Cursor c = centralBankDb.openCursor(null, CursorConfig.DEFAULT);
		while (OperationStatus.SUCCESS == c.getNext(key, data, LockMode.DEFAULT)) {
			String cbId = StringBinding.entryToString(key);
			TupleInput input = TupleBinding.entryToInput(data);
			String name = input.readString();
			Currency ccy = Currency.getInstance(input.readString());
			CentralBank cb = new CentralBank(cbId, name, ccy);
			addCentralBank(cb);
		}
		c.close();
	}

	void loadHolidayDefinitions() throws DatabaseException {
		DatabaseEntry key = new DatabaseEntry(), data = new DatabaseEntry();
		Cursor c = holidayDefinitionDb.openCursor(null, CursorConfig.DEFAULT);
		while (OperationStatus.SUCCESS == c.getNext(key, data, LockMode.DEFAULT)) {
			String hdId = StringBinding.entryToString(key);
			TupleInput input = TupleBinding.entryToInput(data);
			String name = input.readString();
			String desc = input.readString();
			HolidayType type = HolidayType.valueOf(input.readString());
			String expr = input.readString();
			HolidayDefinition hd = new HolidayDefinition(hdId, name, desc, type, expr);
			addHolidayDefinition(hd);
		}
		c.close();
	}

	void loadFinancialCalendars() throws DatabaseException {
		DatabaseEntry key = new DatabaseEntry(), data = new DatabaseEntry();
		Cursor c = financialCalendarDb.openCursor(null, CursorConfig.DEFAULT);
		while (OperationStatus.SUCCESS == c.getNext(key, data, LockMode.DEFAULT)) {
			String fcId = StringBinding.entryToString(key);
			TupleInput input = TupleBinding.entryToInput(data);
			String desc = input.readString();
			CentralBank cb = getCentralBank(input.readString());
			String tokenizedHdIdArray = input.readString();
			String[] hdIdArray = StringUtils.split(tokenizedHdIdArray, ',');
			HolidayDefinition[] hdArray = new HolidayDefinition[hdIdArray.length];
			for (int i = 0; i < hdIdArray.length; i++) {
				hdArray[i] = getHolidayDefinition(hdIdArray[i]);
			}
			FinancialCalendar fc = new FinancialCalendar(fcId, desc, cb, new HashSet<HolidayDefinition>(Arrays.asList(hdArray)));
			addFinancialCalendar(fc);
		}
		c.close();
	}

}
