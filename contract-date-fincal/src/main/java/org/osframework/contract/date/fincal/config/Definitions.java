/*
 * File: Definitions.java
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
package org.osframework.contract.date.fincal.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Abstract superclass of objects which provide the definitions used to generate
 * financial calendar data.
 *
 * @param <S> type of definition source
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class Definitions<S> {

	protected static final String CHECKSUM_ALGORITHM = "MD5";
	protected static final byte[] EMPTY_BYTES = new byte[0];
	protected static final String EMPTY_CHECKSUM = "d41d8cd98f00b204e9800998ecf8427e";

	private Map<String, CentralBank> centralBanks = new HashMap<String, CentralBank>();
	private Map<String, HolidayDefinition> holidayDefinitions = new HashMap<String, HolidayDefinition>();
	private Map<String, FinancialCalendar> financialCalendars = new HashMap<String, FinancialCalendar>();
	private String checksum = EMPTY_CHECKSUM;

	protected S definitionSource;

	/**
	 * Constructor - default called from subclass constructors.
	 */
	protected Definitions() {}

	/**
	 * Constructor - creates deep copy clone of specified
	 * <code>Definitions</code> instance.
	 *
	 * @param originalDefinitions original definitions to be copied
	 */
	public Definitions(Definitions<?> originalDefinitions) {
		this.centralBanks.putAll(originalDefinitions.centralBanks);
		this.holidayDefinitions.putAll(originalDefinitions.holidayDefinitions);
		this.financialCalendars.putAll(originalDefinitions.financialCalendars);
	}

	/**
	 * @return <code>true</code> if this object contains no definitions
	 */
	public boolean isEmpty() {
		return (centralBanks.isEmpty() &&
				holidayDefinitions.isEmpty() &&
				financialCalendars.isEmpty());
	}

	/**
	 * @return calculated checksum of this object's definitions
	 */
	public String getChecksum() {
		return this.checksum;
	}

	public void addCentralBank(CentralBank centralBank) {
		if (!centralBanks.containsKey(centralBank.getId())) {
			centralBanks.put(centralBank.getId(), centralBank);
		}
	}

	public CentralBank getCentralBank(String id) {
		return centralBanks.get(id);
	}

	public void removeCentralBank(CentralBank centralBank) {
		if (centralBanks.containsKey(centralBank.getId())) {
			centralBanks.remove(centralBank.getId());
		}
	}

	public void addHolidayDefinition(HolidayDefinition holiday) {
		if (!holidayDefinitions.containsKey(holiday.getId())) {
			holidayDefinitions.put(holiday.getId(), holiday);
		}
	}

	public HolidayDefinition getHolidayDefinition(String id) {
		return holidayDefinitions.get(id);
	}

	public void removeHolidayDefinition(HolidayDefinition holiday) {
		if (holidayDefinitions.containsKey(holiday.getId())) {
			holidayDefinitions.remove(holiday.getId());
		}
	}

	public void addFinancialCalendar(FinancialCalendar calendar) {
		if (!financialCalendars.containsKey(calendar.getId())) {
			financialCalendars.put(calendar.getId(), calendar);
		}
	}

	public FinancialCalendar getFinancialCalendar(String id) {
		return financialCalendars.get(id);
	}

	public void removeFinancialCalendar(FinancialCalendar calendar) {
		if (financialCalendars.containsKey(calendar.getId())) {
			financialCalendars.remove(calendar.getId());
		}
	}

	/**
	 * Load financial calendar definition objects into memory (ie, populate this
	 * object). This method calculates a checksum of the complete
	 * loaded definition state.
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		this.doLoad();
		this.calculateChecksum();
	}

	/**
	 * Store financial calendar definitions onto persistent data storage. This
	 * method calculates a checksum of the definition state, immediately prior
	 * to storage.
	 * 
	 * @throws Exception
	 */
	public void store() throws Exception {
		this.calculateChecksum();
		this.doStore();
	}

	protected abstract void doLoad() throws Exception;

	protected abstract void doStore() throws Exception;

	protected final void calculateChecksum() {
		SortedSet<String> cbIds = new TreeSet<String>(centralBanks.keySet());
		SortedSet<String> hdIds = new TreeSet<String>(holidayDefinitions.keySet());
		SortedSet<String> fcIds = new TreeSet<String>(financialCalendars.keySet());
		try {
			MessageDigest digest = MessageDigest.getInstance(CHECKSUM_ALGORITHM);
			for (String cbId : cbIds) {
				digest.update(serializeToBytes(centralBanks.get(cbId)));
			}
			for (String hdId : hdIds) {
				digest.update(serializeToBytes(holidayDefinitions.get(hdId)));
			}
			for (String fcId : fcIds) {
				digest.update(serializeToBytes(financialCalendars.get(fcId)));
			}
			this.checksum = new BigInteger(1, digest.digest()).toString(16);
		} catch (Exception e) {
			// TODO Log a warning here?
			this.checksum = EMPTY_CHECKSUM;
		}
	}

	private byte[] serializeToBytes(Object o) {
		byte[] ser;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			ser = baos.toByteArray();
		} catch (IOException ioe) {
			// TODO Log a warning here?
			// TODO Turn 0-length byte array into constant
			ser = EMPTY_BYTES;
		}
		return ser;
	}

}
