/**
 * 
 */
package org.osframework.contract.date.fincal.expression.centralbank;

import java.lang.reflect.Constructor;
import java.util.Iterator;

import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.model.CentralBank;
import org.osframework.util.ServiceClassLoader;

/**
 * @author kzk55j
 *
 */
public class CentralBankDecoratorLocator {

	public static HolidayExpression decorate(final HolidayExpression targetExpr, CentralBank bank) {
		HolidayExpression decoratedExpr = null;
		ServiceClassLoader<CentralBankDecorator> scl = ServiceClassLoader.load(CentralBankDecorator.class);
		Iterator<Class<? extends CentralBankDecorator>> it = scl.iterator();
		while (it.hasNext()) {
			Class<? extends CentralBankDecorator> cbdClass = it.next();
			try {
				Constructor<? extends CentralBankDecorator> c = cbdClass.getConstructor(HolidayExpression.class);
				CentralBankDecorator cbd = c.newInstance(targetExpr);
				if (cbd.supports(bank)) {
					decoratedExpr = cbd;
					break;
				}
			} catch (Exception e) {
				// FIXME Log the exception?
				continue;
			}
		}
		return (null != decoratedExpr) ? decoratedExpr : targetExpr;
	}

}
