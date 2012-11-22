/**
 * File: ContractDateSetBuilder.java
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

import static org.osframework.contract.date.util.DateUtil.isDate;
import static org.osframework.contract.date.util.DateUtil.parseDate;
import static org.osframework.contract.date.util.IMM.isIMMCode;
import static org.osframework.contract.date.util.IMM.toCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.jfin.date.BusinessDayConvention;
import org.jfin.date.Frequency;
import org.jfin.date.holiday.HolidayCalendar;
import org.jfin.date.holiday.HolidayCalendarFactory;
import org.osframework.contract.date.impl.ContractDateSetDefaultImpl;
import org.osframework.contract.date.util.IMM;

/**
 * ContractDateSetBuilder description here.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class ContractDateSetBuilder {

	private static final TimePeriod DEFAULT_EFFECTIVE_TP = new TimePeriod(2, TimeUnit.BUSINESS_DAY);

	private Calendar tradeDate = null, effectiveDate = null, expirationDate = null, maturityDate = null;
	private TimeZone timeZone = null;
	private String effectiveDateNotation = null, expirationDateNotation = null, maturityDateNotation = null;
	private boolean useIMMRoll = false;

	/**
	 * 
	 */
	public ContractDateSetBuilder() {
		this(Calendar.getInstance());
	}

	public ContractDateSetBuilder(Calendar tradeDate) {
		this.setTradeDate(tradeDate);
		this.timeZone = tradeDate.getTimeZone();
	}

	public ContractDateSetBuilder(Date tradeDate) {
		this.setTradeDate(tradeDate);
	}

	public ContractDateSetBuilder(String tradeDate) {
		this.setTradeDate(tradeDate);
	}

	public ContractDateSetBuilder setEffectiveDate(Calendar effectiveDate) {
		if (null == effectiveDate) {
			throw new IllegalArgumentException("argument 'effectiveDate' cannot be null");
		}
		this.effectiveDate = effectiveDate;
		if (null != this.timeZone && !this.timeZone.equals(effectiveDate.getTimeZone())) {
			this.effectiveDate.setTimeZone(timeZone);
		}
		return this;
	}

	public ContractDateSetBuilder setEffectiveDate(Date effectiveDate) {
		if (null == effectiveDate) {
			throw new IllegalArgumentException("argument 'effectiveDate' cannot be null");
		}
		Calendar edCal = Calendar.getInstance();
		edCal.setTime(effectiveDate);
		return this.setEffectiveDate(edCal);
	}

	public ContractDateSetBuilder setEffectiveDate(String effectiveDate) {
		if (null == effectiveDate) {
			throw new IllegalArgumentException("argument 'effectiveDate' cannot be null");
		}
		if (isDate(effectiveDate)) {
			return this.setEffectiveDate(parseDate(effectiveDate));
		} else if (isIMMCode(effectiveDate)) {
			Calendar refDate = (null != this.tradeDate) ? this.tradeDate : Calendar.getInstance();
			this.effectiveDateNotation = effectiveDate;
			this.setIMMRoll();
			return this.setEffectiveDate(toCalendar(effectiveDate, refDate));
		} else {
			this.effectiveDateNotation = effectiveDate;
			return this;
		}
	}

	public ContractDateSetBuilder setExpirationDate(Calendar expirationDate) {
		if (null == expirationDate) {
			throw new IllegalArgumentException("argument 'expirationDate' cannot be null");
		}
		this.expirationDate = expirationDate;
		if (null != this.timeZone && !this.timeZone.equals(expirationDate.getTimeZone())) {
			this.expirationDate.setTimeZone(timeZone);
		}
		return this;
	}

	public ContractDateSetBuilder setExpirationDate(Date expirationDate) {
		if (null == expirationDate) {
			throw new IllegalArgumentException("argument 'expirationDate' cannot be null");
		}
		Calendar edCal = Calendar.getInstance();
		edCal.setTime(expirationDate);
		return this.setExpirationDate(edCal);
	}

	public ContractDateSetBuilder setExpirationDate(String expirationDate) {
		if (null == expirationDate) {
			throw new IllegalArgumentException("argument 'expirationDate' cannot be null");
		}
		if (isDate(expirationDate)) {
			return this.setExpirationDate(parseDate(expirationDate));
		} else if (isIMMCode(expirationDate)) {
			Calendar refDate = (null != this.tradeDate) ? this.tradeDate : Calendar.getInstance();
			this.expirationDateNotation = expirationDate;
			return this.setEffectiveDate(toCalendar(expirationDate, refDate));
		} else {
			this.effectiveDateNotation = expirationDate;
			return this;
		}
	}

	public ContractDateSetBuilder setMaturityDate(Calendar maturityDate) {
		if (null == maturityDate) {
			throw new IllegalArgumentException("argument 'maturityDate' cannot be null");
		}
		this.maturityDate = maturityDate;
		if (null != this.timeZone && !this.timeZone.equals(maturityDate.getTimeZone())) {
			this.maturityDate.setTimeZone(this.timeZone);
		}
		return this;
	}

	public ContractDateSetBuilder setMaturityDate(Date maturityDate) {
		if (null == maturityDate) {
			throw new IllegalArgumentException("argument 'maturityDate' cannot be null");
		}
		Calendar mdCal = Calendar.getInstance();
		mdCal.setTime(maturityDate);
		return this.setMaturityDate(mdCal);
	}

	public ContractDateSetBuilder setMaturityDate(String maturityDate) {
		if (null == maturityDate) {
			throw new IllegalArgumentException("argument 'maturityDate' cannot be null");
		}
		if (isDate(maturityDate)) {
			return this.setMaturityDate(parseDate(maturityDate));
		} else if (isIMMCode(maturityDate)) {
			Calendar refDate = (null != this.tradeDate) ? this.tradeDate : Calendar.getInstance();
			this.maturityDateNotation = maturityDate;
			return this.setEffectiveDate(toCalendar(maturityDate, refDate));
		} else {
			this.maturityDateNotation = maturityDate;
			return this;
		}
	}

	public ContractDateSetBuilder setTradeDate(Calendar tradeDate) {
		if (null == tradeDate) {
			throw new IllegalArgumentException("argument 'tradeDate' cannot be null");
		}
		this.tradeDate = tradeDate;
		if (null != this.timeZone && !this.timeZone.equals(this.tradeDate.getTimeZone())) {
			this.tradeDate.setTimeZone(this.timeZone);
		}
		return this;
	}

	public ContractDateSetBuilder setTradeDate(Date tradeDate) {
		if (null == tradeDate) {
			throw new IllegalArgumentException("argument 'tradeDate' cannot be null");
		}
		Calendar tdCal = Calendar.getInstance();
		tdCal.setTime(tradeDate);
		return this.setTradeDate(tdCal);
	}

	public ContractDateSetBuilder setTradeDate(String tradeDate) {
		if (null == tradeDate) {
			throw new IllegalArgumentException("argument 'tradeDate' cannot be null");
		}
		return this.setTradeDate(parseDate(tradeDate));
	}

	public ContractDateSetBuilder setTimeZone(TimeZone timeZone) {
		if (null == timeZone) {
			throw new IllegalArgumentException("argument 'timeZone' cannot be null");
		}
		this.timeZone = timeZone;
		if (null != this.tradeDate && !this.tradeDate.getTimeZone().equals(this.timeZone)) {
			this.tradeDate.setTimeZone(this.timeZone);
		}
		return this;
	}

	public ContractDateSetBuilder setIMMRoll(boolean useIMMRoll) {
		this.useIMMRoll = useIMMRoll;
		return this;
	}

	public ContractDateSetBuilder setIMMRoll() {
		return this.setIMMRoll(true);
	}

	public ContractDateSet build() {
		Calendar referenceDate = (Calendar)this.tradeDate.clone();
		HolidayCalendar<?> hc = null;
		if (mustCalculateEffectiveDate()) {
			TimePeriod effTP = new TimePeriod(this.effectiveDateNotation);
			hc = createHolidayCalendar();
			// Is effectiveDate notation denominated in a larger unit than days?
			// If so, advance reference date by 2B first
			if (TimeUnit.BUSINESS_DAY != effTP.getTimeUnit() && TimeUnit.DAY != effTP.getTimeUnit()) {
				int amount = DEFAULT_EFFECTIVE_TP.getAmount();
				int pu = periodUnitOf(DEFAULT_EFFECTIVE_TP);
				referenceDate = hc.advance(referenceDate, amount, pu, BusinessDayConvention.MODIFIED_FOLLOWING);
			}
			this.effectiveDate = hc.advance(referenceDate, effTP.getAmount(), periodUnitOf(effTP), BusinessDayConvention.MODIFIED_FOLLOWING);
		}
		if (mustCalculateExpirationDate()) {
			TimePeriod expTP = new TimePeriod(this.expirationDateNotation);
			if (null == hc) {
				hc = createHolidayCalendar();
			}
			referenceDate = (Calendar)this.tradeDate.clone();
			this.expirationDate = hc.advance(referenceDate, expTP.getAmount(), periodUnitOf(expTP), BusinessDayConvention.MODIFIED_FOLLOWING);
		}
		if (mustCalculateMaturityDate()) {
			if (null == this.effectiveDate) {
				throw new IllegalStateException("Cannot calculate maturityDate without established effectiveDate");
			}
			TimePeriod matTP = new TimePeriod(this.maturityDateNotation);
			if (this.useIMMRoll && (TimeUnit.MONTH != matTP.getTimeUnit() || TimeUnit.YEAR == matTP.getTimeUnit())) {
				this.maturityDate = calculateMaturityIMMDate(matTP);
			} else {
				if (null == hc) {
					hc = createHolidayCalendar();
				}
				this.maturityDate = hc.advance(this.effectiveDate, matTP.getAmount(), periodUnitOf(matTP), BusinessDayConvention.MODIFIED_FOLLOWING);
			}
		}
		return new ContractDateSetDefaultImpl(this.tradeDate, this.effectiveDate, this.expirationDate, this.maturityDate);
	}

	private Calendar calculateMaturityIMMDate(TimePeriod mtp) {
		// Get year of effectiveDate
		Calendar referenceDate = (Calendar)this.effectiveDate.clone(),
				 calcMaturityDate = null;
		switch (mtp.getTimeUnit()) {
		case MONTH:
			referenceDate.add(Calendar.MONTH, mtp.getAmount());
			referenceDate.set(Calendar.DAY_OF_MONTH, 1);
			calcMaturityDate = IMM.nextCalendar(referenceDate, false);
			break;
		case YEAR:
			referenceDate.add(Calendar.YEAR, mtp.getAmount());
			referenceDate.set(Calendar.DAY_OF_MONTH, 1);
			calcMaturityDate = IMM.nextCalendar(referenceDate, false);
			break;
		default:
			throw new IllegalArgumentException("Cannot calculate IMM date from time unit: " + mtp.getTimeUnit());
		}
		return calcMaturityDate;
	}

	private boolean mustCalculateEffectiveDate() {
		return (null == this.effectiveDate && null != this.effectiveDateNotation);
	}

	private boolean mustCalculateExpirationDate() {
		return (null == this.expirationDate && null != this.expirationDateNotation);
	}

	private boolean mustCalculateMaturityDate() {
		return (null == this.maturityDate && null != this.maturityDateNotation);
	}

	private HolidayCalendar<?> createHolidayCalendar() {
		return HolidayCalendarFactory.newInstance().getHolidayCalendar("WE");
	}

	private int periodUnitOf(TimePeriod tp) {
		TimeUnit tu = tp.getTimeUnit();
		Frequency f = null;
		switch (tu) {
		case BUSINESS_DAY:
		case DAY:
			f = Frequency.DAILY;
			break;
		case WEEK:
			f = Frequency.WEEKLY;
			break;
		case MONTH:
			f = Frequency.MONTHLY;
			break;
		case QUARTER:
			f = Frequency.QUARTERLY;
			break;
		case YEAR:
			f = Frequency.ANNUALLY;
			break;
		default:
			throw new IllegalArgumentException("Invalid TimePeriod unit: " + tu);
		}
		return f.getPeriodUnit();
	}

}
