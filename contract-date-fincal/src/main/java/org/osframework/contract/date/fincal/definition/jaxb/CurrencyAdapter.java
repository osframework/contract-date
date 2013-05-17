/*
 * File: CurrencyAdapter.java
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
package org.osframework.contract.date.fincal.definition.jaxb;

import java.util.Currency;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for marshaling {@link Currency} to XML and vice versa.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class CurrencyAdapter extends XmlAdapter<String, Currency> {

	public Currency unmarshal(String currencyCode) throws Exception {
		return Currency.getInstance(currencyCode);
	}

	public String marshal(Currency currency) throws Exception {
		return currency.getCurrencyCode();
	}

}
