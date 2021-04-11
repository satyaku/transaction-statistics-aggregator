package com.n26.utility;

import java.math.BigDecimal;

import com.n26.constants.ExceptionConstants;
import com.n26.exceptions.EntityNotParsableException;

public class BigDecimalUtils {

	public static BigDecimal getBigDecimalValue(String amount) {
		try {

			BigDecimal bigDecimalValue = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
			return bigDecimalValue;

		} catch (NumberFormatException e) {
			throw new EntityNotParsableException(ExceptionConstants.UNPARSABLE_ENTITY + " Amount");
		}

	}

}
