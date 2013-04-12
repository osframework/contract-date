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
import org.osframework.contract.date.fincal.ImmutableEntity;

/**
 * Public institution that manages a country's currency, money supply, and
 * interest rates. Central banks also usually oversee the commercial banking
 * system of their respective countries.
 * <p>An instance of this class can provide an immutable and thread-safe
 * version of itself.</p>
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CentralBank implements Serializable, ImmutableEntity<CentralBank> {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 4797279366774872436L;

	private String id;
	private String name;
	private String country;
	private Currency currency;

	/**
	 * Default constructor.
	 */
	public CentralBank() {}

	/**
	 * Constructor.
	 *
	 * @param id unique identifier for this instance
	 * @param name name of this central bank
	 * @param country ISO-3166 country code for this central bank
	 * @param currency currency managed by this central bank
	 * @throws IllegalArgumentException if <code>country</code> is not valid
	 *         ISO-3166 alpha2 code or if <code>currency</code> is null
	 */
	public CentralBank(final String id,
			           final String name,
			           final String country,
			           final Currency currency) {
		this();
		this.setId(id);
		this.setName(name);
		this.setCountry(country);
		this.setCurrency(currency);
	}

	/**
	 * Constructor.
	 *
	 * @param id unique identifier for this instance
	 * @param name name of this central bank
	 * @param country ISO-3166 country code for this central bank
	 * @param currency ISO-4217 code for currency managed by this central bank
	 * @throws IllegalArgumentException if <code>country</code> is not valid
	 *         ISO-3166 alpha2 code or if <code>currencyCode</code> is not valid
	 *         ISO-4217 code
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
	 * Set unique identifier for this central bank.
	 * 
	 * @param id unique identifier for this instance
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return name of this central bank
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of this central bank.
	 * 
	 * @param name name of this central bank
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return ISO-3166 country code for this central bank
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Set ISO-3166 country code for this central bank.
	 * 
	 * @param country ISO-3166 country code for this central bank
	 * @throws IllegalArgumentException if country is not a supported ISO-3166
	 *         alpha2 country code
	 */
	public void setCountry(String country) {
		if (StringUtils.isBlank(country) ||
			(2 != StringUtils.length(country.trim()))) {
			throw new IllegalArgumentException("Invalid ISO-3166 alpha2 country code");
		}
		this.country = StringUtils.upperCase(country.trim(), Locale.ENGLISH);
	}

	/**
	 * @return currency managed by this central bank
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Set currency managed by this central bank.
	 * 
	 * @param currency currency managed by this central bank
	 * @throws IllegalArgumentException if currency is null
	 */
	public void setCurrency(Currency currency) {
		if (null == currency) {
			throw new IllegalArgumentException("Currency argument cannot be null");
		}
		this.currency = currency;
	}

	/**
	 * Set currency managed by this central bank.
	 * 
	 * @param currencyCode ISO-4217 code of currency managed by this central bank
	 * @throws IllegalArgumentException if currencyCode is not a supported
	 *         ISO-4217 code
	 */
	public void setCurrency(String currencyCode) {
		this.setCurrency(Currency.getInstance(currencyCode));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public CentralBank toImmutable() {
		return new ImmutableCentralBank(this.id, this.name, this.country, this.currency);
	}

	public boolean isImmutable() {
		return (this instanceof ImmutableCentralBank);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
                   .append(id)
                   .append(name)
                   .append(country)
                   .append(currency)
                   .toHashCode();
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

	private final class ImmutableCentralBank extends CentralBank {
	
		/**
		 * Serializable UID.
		 */
		private static final long serialVersionUID = -5118309327834501135L;
	
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
		 * @param currency ISO-4217 code for currency managed by this central bank
		 * @throws IllegalArgumentException if any argument is null or empty, or if
		 *         <code>country</code> is not valid ISO-3166 alpha2 code, or if
		 *         <code>currency</code> is null
		 */
		ImmutableCentralBank(final String id,
				             final String name,
				             final String country,
				             final Currency currency) {
			super();
			super.setId(id);
			super.setName(name);
			super.setCountry(country);
			super.setCurrency(currency);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setId(String id) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setName(String name) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setCountry(String country) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setCurrency(Currency currency) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * Overridden method to prevent mutability of this instance.
		 * 
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void setCurrency(String currencyCode) {
			throw new UnsupportedOperationException(DEFAULT_EXCEPTION_MSG);
		}

		/**
		 * This implementation simply returns <code>this</code>.
		 */
		@Override
		public final CentralBank toImmutable() {
			return this;
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
	}

}
