/**
 * 
 */
package org.osframework.contract.date.fincal.expression.centralbank;

import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.model.CentralBank;

/**
 * @author kzk55j
 *
 */
public class CentralBankDecoratorLocator {

	public static HolidayExpression decorate(final HolidayExpression targetExpr, CentralBank bank) {
		if (BankOfEnglandDecorator.CENTRAL_BANK.equals(bank.getId())) {
			return new BankOfEnglandDecorator(targetExpr);
		} else if (USFederalReserveDecorator.CENTRAL_BANK.equals(bank.getId())) {
			return new USFederalReserveDecorator(targetExpr);
		} else {
			return targetExpr;
		}
	}

}
