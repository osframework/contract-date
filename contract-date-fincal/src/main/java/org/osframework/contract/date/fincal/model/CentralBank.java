/*
 * File: CentralBank.java
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
package org.osframework.contract.date.fincal.model;

import java.io.Serializable;
import java.util.Currency;

import org.osframework.util.HashCodeUtil;

/**
 * Public institution that manages a country's currency, money supply, and
 * interest rates. Central banks also usually oversee the commercial banking
 * system of their respective countries.
 * <p>Instances of this class are immutable and thread-safe.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CentralBank implements Serializable {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 6631022375355172261L;

	private final String id;
	private final String name;
	private final Currency currency;

	private transient int hashCode;

	public CentralBank(final String id, final String name, final Currency currency) {
		this.id = id;
		this.name = name;
		this.currency = currency;
	}

	public CentralBank(final String id, final String name, final String currencyCode) {
		this(id, name, Currency.getInstance(currencyCode));
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("CentralBank[id='").append(id)
		                        .append("', name='").append(name)
		                        .append("', currency='").append(currency)
		                        .append("']");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash(result, this.id);
			result = HashCodeUtil.hash(result, this.name);
			result = HashCodeUtil.hash(result, this.currency);
			this.hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		CentralBank other = (CentralBank) obj;
		return ((null == id ? null == other.id : id.equals(other.id)) &&
				(null == name ? null == other.name : name.equals(other.name)) &&
				(currency == other.currency));
	}

}
