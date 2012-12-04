/**
 * File: DateUtil.java
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
package org.osframework.contract.date.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * DateUtil description here.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class DateUtil {

	public static final int DATE_ISO8601    = 0;
	public static final int DATE_US         = 1;
	public static final int DATE_US_REVERSE = 2;

	public static final String REGEX_DATE_ISO8601 = "\\d{4}-\\d{2}-\\d{2}";
	public static final String REGEX_DATE_US = "\\d{2}/\\d{2}/\\d{4}";
	public static final String REGEX_DATE_US_REVERSE = "\\d{4}/\\d{2}/\\d{2}";

	public static final Pattern PATTERN_DATE_ISO8601 = Pattern.compile(REGEX_DATE_ISO8601);
	public static final Pattern PATTERN_DATE_US = Pattern.compile(REGEX_DATE_US);
	public static final Pattern PATTERN_DATE_US_REVERSE = Pattern.compile(REGEX_DATE_US_REVERSE);

	public static final DateFormat FORMAT_DATE_ISO8601 = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat FORMAT_DATE_US = new SimpleDateFormat("MM/dd/yyyy");
	public static final DateFormat FORMAT_DATE_US_REVERSE = new SimpleDateFormat("yyyy/MM/dd");

	// private static final String[] REGEX_ARRAY = { REGEX_DATE_ISO8601, REGEX_DATE_US, REGEX_DATE_US_REVERSE };
	private static final Pattern[] PATTERN_ARRAY = { PATTERN_DATE_ISO8601, PATTERN_DATE_US, PATTERN_DATE_US_REVERSE };
	private static final DateFormat[] FORMAT_ARRAY = { FORMAT_DATE_ISO8601, FORMAT_DATE_US, FORMAT_DATE_US_REVERSE };

	private DateUtil() {}

	public static boolean isDate(String s) {
		if (StringUtils.isBlank(s)) return false;
		return (-1 != findArrayIndex(s));
	}

	public static Date parseDate(String s) {
		int idx = findArrayIndex(s);
		if (-1 == idx) {
			throw new IllegalArgumentException("Invalid date string: " + s);
		}
		DateFormat f = FORMAT_ARRAY[idx];
		try {
			return f.parse(s);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date string: " + s);
		}
	}

	public static String formatDate(Date d, int formatIdx) {
		if (null == d) {
			throw new IllegalArgumentException("Date argument cannot be null");
		}
		if (0 > formatIdx || 2 < formatIdx) {
			throw new IllegalArgumentException("Bad format constant argument");
		}
		return FORMAT_ARRAY[formatIdx].format(d);
	}

	public static String formatDateToISO8601(Date d) {
		return formatDate(d, DATE_ISO8601);
	}

	public static String formatDateToUS(Date d) {
		return formatDate(d, DATE_US);
	}

	public static String formatDateToUSReverse(Date d) {
		return formatDate(d, DATE_US_REVERSE);
	}

	public static int toInt(Date d) {
		String s = formatDateToISO8601(d);
		return Integer.parseInt(s.replace("-", ""));
	}

	private static int findArrayIndex(String s) {
		int idx = -1;
		for (int i = 0; i < PATTERN_ARRAY.length; i++) {
			Pattern p = PATTERN_ARRAY[i];
			if (p.matcher(s).matches()) {
				idx = i;
				break;
			}
		}
		return idx;
	}

}
