/**
 * File: ContractDateSet.java
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
package org.osframework.contract.date;

import java.util.Date;

/**
 * Basic contract of objects that provide the essential major dates on a
 * derivative contract trade.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public interface ContractDateSet {

	/**
	 * @return date of trade execution
	 */
	public Date getTradeDate();

	/**
	 * @return date on which contract starts
	 */
	public Date getStartDate();

	/**
	 * @return date on which contract ends
	 */
	public Date getEndDate();

}
