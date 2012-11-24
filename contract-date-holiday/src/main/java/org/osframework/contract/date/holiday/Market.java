/**
 * File: Market.java
 * 
 * Copyright 2012 David A. Joyce, OSFramework Project.
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
package org.osframework.contract.date.holiday;

import java.io.Serializable;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

/**
 * World financial market center.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class Market implements Serializable {

	private static final long serialVersionUID = -3048725800626292096L;

	private final String id;
	private final String description;
	private final Set<Currency> currencies = new HashSet<Currency>();

	public Market(final String id, final String description) {
		this.id = id;
		this.description = description;
	}

	public Market(final String id, final String description, final Currency... currencies) {
		this(id, description);
		if (null != currencies && 0 < currencies.length) {
			for (Currency ccy : currencies) {
				this.currencies.add(ccy);
			}
		}
	}

	public Market(final String id, final String description, final String... currencies) {
		this(id, description);
		if (null != currencies && 0 < currencies.length) {
			for (String ccyCode : currencies) {
				Currency ccy = Currency.getInstance(ccyCode);
				this.currencies.add(ccy);
			}
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the currencies
	 */
	public Set<Currency> getCurrencies() {
		return currencies;
	}

}
