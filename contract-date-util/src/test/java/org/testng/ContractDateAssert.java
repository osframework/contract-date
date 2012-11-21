/**
 * File: ContractDateAssert.java
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
package org.testng;

import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;

/**
 * ContractDateAssert description here.
 * 
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class ContractDateAssert extends Assert {

	protected ContractDateAssert() {
		super();
	}

	public static void assertSameDay(Date actual, Date expected, String message) {
		if ((expected == null) && (actual == null)) {
			return;
		}
		if (null != expected) {
			Date expectedStripped = stripTime(expected),
				 actualStripped = (null == actual) ? null : stripTime(actual);
			if (expectedStripped.equals(actualStripped)) {
				return;
			}
		}
		fail(format(actual, expected, message));
	}

	private static Date stripTime(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

}
