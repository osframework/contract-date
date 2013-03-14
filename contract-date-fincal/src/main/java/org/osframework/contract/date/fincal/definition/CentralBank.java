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
package org.osframework.contract.date.fincal.definition;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
	private static final long serialVersionUID = 4094026015917601847L;

	private final String id;
	private final String name;
	private final String country;
	private final Currency currency;

	/**
	 * Cached hash value for this instance.
	 */
	private volatile transient int hashCode;

	/**
	 * Constructor.
	 *
	 * @param id unique identifier for this instance
	 * @param name name of this central bank
	 * @param country ISO-3166 country code for this central bank
	 * @param currency currency managed by this central bank
	 * @throws IllegalArgumentException if any argument is null or empty, or if
	 *         <code>country</code> is not valid ISO-3166 alpha2 code
	 */
	public CentralBank(final String id,
			           final String name,
			           final String country,
			           final Currency currency) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("Invalid blank 'id' argument");
		}
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Invalid blank 'name' argument");
		}
		if (2 != StringUtils.length(country)) {
			throw new IllegalArgumentException("Invalid ISO-3166 alpha2 country code");
		}
		if (null == currency) {
			throw new IllegalArgumentException("Currency argument cannot be null");
		}
		this.id = id;
		this.name = name;
		this.country = StringUtils.upperCase(country, Locale.ENGLISH);
		this.currency = currency;
	}

	/**
	 * Constructor.
	 *
	 * @param id unique identifier for this instance
	 * @param name name of this central bank
	 * @param country ISO-3166 country code for this central bank
	 * @param currency ISO-4217 code for currency managed by this central bank
	 * @throws IllegalArgumentException if any argument is null or empty, or if
	 *         <code>country</code> is not valid ISO-3166 alpha2 code, or if
	 *         <code>currencyCode</code> is not valid ISO-4217 code
	 */
	public CentralBank(final String id,
			           final String name,
			           final String country,
			           final String currencyCode) {
		this(id, name, country, Currency.getInstance(currencyCode));
	}

	/**
	 * @return unique identifier for this instance
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return name of this central bank
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return ISO-3166 country code for this central bank
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return currency managed by this central bank
	 */
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = new HashCodeBuilder()
			               .append(id)
			               .append(name)
			               .append(country)
			               .append(currency)
			               .toHashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals;
		if (this == obj) {
			equals = true;
		} else if (obj instanceof CentralBank) {
			final CentralBank other = (CentralBank)obj;
			equals = new EqualsBuilder()
			             .append(id, other.id)
			             .append(name, other.name)
			             .append(country, other.country)
			             .append(currency, other.currency)
			             .isEquals();
		} else {
			equals = false;
		}
		return equals;
	}

}
