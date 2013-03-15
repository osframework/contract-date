/**
 * File: ContractDateSetDefaultImpl.java
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
package org.osframework.contract.date.impl;

import java.util.Calendar;
import java.util.Date;

import org.osframework.contract.date.ContractDateSet;

/**
 * Default implementation of <code>ContractDateSet</code> interface.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class ContractDateSetDefaultImpl implements ContractDateSet {

	private final Date tradeDate, effectiveDate, expirationDate, maturityDate;

	/**
	 * 
	 */
	public ContractDateSetDefaultImpl(Date tradeDate,
			                          Date effectiveDate,
			                          Date expirationDate,
			                          Date maturityDate) {
		this.tradeDate = tradeDate;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.maturityDate = maturityDate;
	}

	public ContractDateSetDefaultImpl(Calendar tradeDate,
			                          Calendar effectiveDate,
			                          Calendar expirationDate,
			                          Calendar maturityDate) {
		this.tradeDate = (null != tradeDate) ? tradeDate.getTime() : null;
		this.effectiveDate = (null != effectiveDate) ? effectiveDate.getTime() : null;
		this.expirationDate = (null != expirationDate) ? expirationDate.getTime() : null;
		this.maturityDate = (null != maturityDate) ? maturityDate.getTime() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getTradeDate() {
		return tradeDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getStartDate() {
		return effectiveDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getEndDate() {
		return maturityDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

}
