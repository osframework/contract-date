/**
 * File: TimeUnit.java
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
package org.osframework.contract.date;

/**
 * Enumeration of conventional time units used in notation of contract term.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public enum TimeUnit {

	BUSINESS_DAY('B'),
	DAY('D'),
	WEEK('W'),
	MONTH('M'),
	QUARTER('Q'),
	YEAR('Y');

	private final char symbol;

	private TimeUnit(char symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the market standard symbol representing this
	 *         <code>TimeUnit</code>
	 */
	public char getSymbol() {
		return symbol;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(this.name().substring(0, 1))
		                        .append(this.name().substring(1).toLowerCase().replace('_', ' '));
		return buf.toString();
	}

	public static TimeUnit valueOf(char symbol) {
		TimeUnit[] tuArr = values();
		for (TimeUnit tu : tuArr) {
			if (tu.symbol == symbol) {
				return tu;
			}
		}
		throw new IllegalArgumentException("Invalid TimeUnit symbol - '" + symbol + "'");
	}

}
