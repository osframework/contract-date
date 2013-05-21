/*
 * File: WesternEaster.java
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
package org.osframework.contract.date.fincal.definition.algorithm;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Provides computation of Easter Sunday by the "Western" definition (ie, Roman
 * Catholic and most Protestant churches). British, European, and some Asian
 * markets use this date for observance of Easter-relative bank holidays.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@XmlTransient
public class WesternEaster implements HolidayAlgorithm {

	public static final String NAME = "EASTER";

	/**
	 * Constructor.
	 */
	public WesternEaster() {}

	/**
	 * {@inheritDoc}
	 */
	public String name() {
		return NAME;
	}

	/**
	 * Compute the date of Easter Sunday for the given year.
	 * 
	 * @param year year for which this algorithm must compute the holiday date
	 * @return date of Easter Sunday in the given year
	 */
	public Date compute(int year) {
		int a, b, c, d, e, f, g, h, i, j, k, l;
		int easterMonth, pfm, easterDayOfMonth;
		
		a = year % 19;
		b = year / 100;
		c = year % 100;
		d = b / 4;
		e = b % 4;
		f = (b + 8) / 25;
		g = (b - f + 1) / 3;
		h = (19 * a + b - d - g + 15) % 30;
		i = c / 4;
		j = c % 4;
		k = (32 + 2 * e + 2 * i - h - j) % 7;
		l = (a + 11 * h + 22 * k) / 451;
		
		// 1-based index of Easter month
		easterMonth = (h + k - 7 * l + 114) / 31;
		pfm = (h + k - 7 * l + 114) % 31;
		
		// Day in Easter month
		easterDayOfMonth = pfm + 1;
		
		Calendar easter = Calendar.getInstance();
		easter.set(Calendar.YEAR, year);
		easter.set(Calendar.MONTH, (easterMonth - 1));
		easter.set(Calendar.DAY_OF_MONTH, easterDayOfMonth);
		return easter.getTime();
	}

}
