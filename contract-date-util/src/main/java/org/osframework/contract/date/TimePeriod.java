/**
 * File: TimePeriod.java
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

/**
 * An immutable representation of a finite period of time, denominated in a
 * particular {@link TimeUnit}.
 * <p>Examples:</p>
 * <table border="1">
 * 	<tr>
 * 		<th>Notation</th>
 * 		<th>&nbsp;</th>
 * 		<th>Amount</th>
 * 		<th>TimeUnit</th>
 * 	</tr>
 * 	<tr>
 * 		<td>2B</td>
 * 		<td>=</td>
 * 		<td>2</td>
 * 		<td>BUSINESS_DAY</td>
 * 	</tr>
 * 	<tr>
 * 		<td>3M</td>
 * 		<td>=</td>
 * 		<td>3</td>
 * 		<td>MONTH</td>
 * 	</tr>
 * 	<tr>
 * 		<td>10Y</td>
 * 		<td>=</td>
 * 		<td>10</td>
 * 		<td>YEAR</td>
 * 	</tr>
 * </table>
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class TimePeriod {

	private final int amount;
	private final TimeUnit timeUnit;

	public TimePeriod(String notatedTimePeriod) {
		String ntp = (null == notatedTimePeriod) ? null : notatedTimePeriod.trim();
		if (null == ntp || 2 > ntp.length()) {
			throw new IllegalArgumentException("argument 'notatedTimePeriod' must be 2 or more non-whitespace characters");
		}
		StringBuilder iBuf = new StringBuilder(),
		              cBuf = new StringBuilder();
		char[] ntpArr = ntp.toCharArray();
		for (int i = 0; i < ntpArr.length; i++) {
			if (Character.isWhitespace(ntpArr[i])) {
				continue;
			} else if ('-' == ntpArr[i]) {
				if (0 == i) {
					iBuf.append(ntpArr[i]);
					continue;
				} else {
					throw new IllegalArgumentException("Invalid 'notatedTimePeriod' argument: " + notatedTimePeriod);
				}
			} else if (Character.isDigit(ntpArr[i])) {
				if (0 == i) {
					iBuf.append(ntpArr[i]);
				} else {
					char prevChar = ntpArr[i-1];
					if ('-' != prevChar && !Character.isDigit(prevChar)) {
						throw new IllegalArgumentException("Invalid 'notatedTimePeriod' argument: " + notatedTimePeriod);
					}
					iBuf.append(ntpArr[i]);
				}
			} else if (Character.isLetter(ntpArr[i])) {
				if (0 == i) {
					throw new IllegalArgumentException("Invalid 'notatedTimePeriod' argument: " + notatedTimePeriod);
				}
				cBuf.append(ntpArr[i]);
			}
		}
		int amount = Integer.parseInt(iBuf.toString());
		char symbol = cBuf.toString().toUpperCase().charAt(0);
		this.amount = amount;
		this.timeUnit = TimeUnit.valueOf(symbol);
	}

	public TimePeriod(int amount, char timeUnitSymbol) {
		this(amount, TimeUnit.valueOf(timeUnitSymbol));
	}

	public TimePeriod(int amount, TimeUnit timeUnit) {
		this.amount = amount;
		this.timeUnit = timeUnit;
	}

	/**
	 * @return the number of <code>timeUnit</code>s in this period
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @return the conventional time unit
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

}
