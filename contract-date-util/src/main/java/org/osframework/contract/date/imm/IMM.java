/**
 * File: IMM.java
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
package org.osframework.contract.date.imm;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for identification and calculation of valid International
 * Money Market (IMM) dates.
 * <p><i>Main cycle</i> IMM dates fall in the months of March, June, September,
 * and December. These months are the ending months of the fiscal quarters of
 * the standard calendar year.</p>
 * <p>IMM dates are typically written or notated as a 2-character code: a single
 * letter followed by a single digit, 0 - 9. The letter represents the
 * <i>desired month</i>; the digit represents the <i>desired year</i> (desired
 * year mod 10).</p>
 * <p>The IMM code months are:</p>
 * <table border="1">
 * 	<tr>
 * 		<th>Letter</th>
 * 		<th>Month</th>
 * 	</tr>
 * 	<tr>
 * 		<td>F</td>
 * 		<td>January</td>
 * 	</tr>
 * 	<tr>
 * 		<td>G</td>
 * 		<td>February</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>H</b></td>
 * 		<td><b>March</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>J</td>
 * 		<td>April</td>
 * 	</tr>
 * 	<tr>
 * 		<td>K</td>
 * 		<td>May</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>M</b></td>
 * 		<td><b>June</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>N</td>
 * 		<td>July</td>
 * 	</tr>
 * 	<tr>
 * 		<td>Q</td>
 * 		<td>August</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>U</b></td>
 * 		<td><b>September</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>V</td>
 * 		<td>October</td>
 * 	</tr>
 * 	<tr>
 * 		<td>X</td>
 * 		<td>November</td>
 * 	</tr>
 * 	<tr>
 * 		<td><b>Z</b></td>
 * 		<td><b>December</b></td>
 * 	</tr>
 * </table>
 *
 * @see <a href="http://en.wikipedia.org/wiki/IMM_dates">IMM dates</a>
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class IMM {

	private static final String YEARS = "0123456789";
	private static final String MONTHS_MAIN_CYCLE = "hmuzHMUZ";
	private static final String MONTHS_FULL_CYCLE = "fghjkmnquvxzFGHJKMNQUVXZ";

	private IMM() {}

	/**
	 * Determine if the specified string is a valid IMM date code.
	 * 
	 * @param immCode IMM date code to be validated
	 * @return <code>true</code> if string is a valid IMM date code in the
	 *         <i>Full cycle</i>, <code>false</code> otherwise
	 */
	public static boolean isIMMCode(final String immCode) {
		return isIMMCode(immCode, false);
	}

	/**
	 * Determine if the specified string is a valid IMM date code.
	 * 
	 * @param immCode IMM date code to be validated
	 * @param mainCycle flag indicating whether to validate <code>immCode</code>
	 *                  against <i>Main cycle</i> or <i>Full cycle</i> months
	 * @return <code>true</code> if string is a valid IMM date code in the
	 *         specified cycle, <code>false</code> otherwise
	 */
	public static boolean isIMMCode(final String immCode, final boolean mainCycle) {
		if (null == immCode || 2 != immCode.length()) return false;
		if (-1 == YEARS.indexOf(immCode.charAt(1))) return false;
		String months = (mainCycle) ? MONTHS_MAIN_CYCLE : MONTHS_FULL_CYCLE;
		if (-1 == months.indexOf(immCode.charAt(0))) return false;
		return true;
	}

	/**
	 * Determine if the specified date is a valid IMM date.
	 * 
	 * @param date date to be validated
	 * @return <code>true</code> if date is a valid IMM date in the
	 *         <i>Full cycle</i>, <code>false</code> otherwise
	 */
	public static boolean isIMMDate(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return isIMMDate(c);
	}

	/**
	 * Determine if the specified date is a valid IMM date.
	 * 
	 * @param date date to be validated
	 * @param mainCycle flag indicating whether to validate <code>immCode</code>
	 *                  against <i>Main cycle</i> or <i>Full cycle</i> months
	 * @return <code>true</code> if date is a valid IMM date in the
	 *         specified cycle, <code>false</code> otherwise
	 */
	public static boolean isIMMDate(final Date date, final boolean mainCycle) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return isIMMDate(c, mainCycle);
	}

	public static boolean isIMMDate(final Calendar date) {
		return isIMMDate(date, false);
	}

	public static boolean isIMMDate(final Calendar date, final boolean mainCycle) {
		if (Calendar.WEDNESDAY != date.get(Calendar.DAY_OF_WEEK)) return false;
		int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
		if (15 > dayOfMonth || 21 < dayOfMonth) return false;
		if (!mainCycle) {
			return true;
		} else {
			int month = date.get(Calendar.MONTH);
			switch (month) {
			case Calendar.MARCH:
			case Calendar.JUNE:
			case Calendar.SEPTEMBER:
			case Calendar.DECEMBER:
				return true;
			default:
				return false;
			}
		}
	}

	/**
	 * Convert the specified IMM date code to a valid IMM date, using today as
	 * a starting point reference date.
	 * 
	 * @param immCode IMM date code to be converted
	 * @return valid IMM date, relative to today
	 */
	public static Date toDate(final String immCode) {
		return toDate(immCode, new Date());
	}

	/**
	 * Convert the specified IMM date code to a valid IMM date, using given
	 * starting point reference date.
	 * 
	 * @param immCode IMM date code to be converted
	 * @param referenceDate starting point reference date
	 * @return valid IMM date, relative to reference date
	 */
	public static Date toDate(final String immCode, final Date referenceDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(referenceDate);
		Calendar immDate = toCalendar(immCode, c);
		return immDate.getTime();
	}

	public static Calendar toCalendar(final String immCode) {
		return toCalendar(immCode, Calendar.getInstance());
	}

	public static Calendar toCalendar(final String immCode, final Calendar referenceDate) {
		if (!isIMMCode(immCode)) {
			throw new IllegalArgumentException("Invalid IMM date code: " + immCode);
		}
		int m = getMonthConstant(immCode.charAt(0));
		int y = immCode.charAt(1) - '0';
		
		/*
		 * Years prior or equal to 1909 are not valid IMM years; pre-adjustment
		 * forward is necessary
		 */
		int rdYear = referenceDate.get(Calendar.YEAR);
		if (0 == y && 1909 >= rdYear) {
			y += 10;
		}
		int yMod = rdYear % 10;
		y += (rdYear - yMod);
		Calendar c = Calendar.getInstance();
		c.set(y, m, 1);
		Calendar result = nextCalendar(c, false);
		if (result.before(referenceDate)) {
			c.add(Calendar.YEAR, 10);
			result = nextCalendar(c, false);
		}
		return result;
	}

	public static Date nextDate(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar result = nextCalendar(c);
		return result.getTime();
	}

	public static Date nextDate(final Date date, final boolean mainCycle) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar result = nextCalendar(c, mainCycle);
		return result.getTime();
	}

	public static Calendar nextCalendar(final Calendar date) {
		return nextCalendar(date, true);
	}

	public static Calendar nextCalendar(final Calendar date, final boolean mainCycle) {
		Calendar c = (null == date) ? Calendar.getInstance() : date;
		int y = c.get(Calendar.YEAR);
		// Add 1 since Calendar months are 0-based indexes
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int offset = mainCycle ? 3 : 1;
		int skipMonths = offset - (m % offset);
		if (skipMonths != offset || d > 21) {
			skipMonths += m;
			if (12 >= skipMonths) {
				m = skipMonths;
			} else {
				m = skipMonths - 12;
				y += 1;
			}
		}
		Calendar increment = Calendar.getInstance();
		int dayOfMonth = 1, wed = 0;
		increment.set(y, (m - 1), dayOfMonth);
		while (wed < 3) {
			int weekday = increment.get(Calendar.DAY_OF_WEEK);
			if (Calendar.WEDNESDAY == weekday) {
				wed += 1;
			}
			if (3 == wed) {
				break;
			} else {
				increment.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		Calendar result = increment;
		if (result.before(date) || result.equals(date)) {
			increment.set(Calendar.DAY_OF_MONTH, 22);
			increment.set(Calendar.MONTH, (m - 1));
			increment.set(Calendar.YEAR, y);
			result = nextCalendar(increment, mainCycle);
		}
		return result;
	}

	private static int getMonthConstant(char monthLetter) {
		char upper = Character.toUpperCase(monthLetter);
		int monthConstant;
		switch (upper) {
		case 'F':
			monthConstant = Calendar.JANUARY;
			break;
		case 'G':
			monthConstant = Calendar.FEBRUARY;
			break;
		case 'H':
			monthConstant = Calendar.MARCH;
			break;
		case 'J':
			monthConstant = Calendar.APRIL;
			break;
		case 'K':
			monthConstant = Calendar.MAY;
			break;
		case 'M':
			monthConstant = Calendar.JUNE;
			break;
		case 'N':
			monthConstant = Calendar.JULY;
			break;
		case 'Q':
			monthConstant = Calendar.AUGUST;
			break;
		case 'U':
			monthConstant = Calendar.SEPTEMBER;
			break;
		case 'V':
			monthConstant = Calendar.OCTOBER;
			break;
		case 'X':
			monthConstant = Calendar.NOVEMBER;
			break;
		case 'Z':
			monthConstant = Calendar.DECEMBER;
			break;
		default:
			throw new IllegalArgumentException("Invalid IMM month letter: " + monthLetter);
		}
		return monthConstant;
	}

}
