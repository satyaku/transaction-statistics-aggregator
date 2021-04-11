package com.n26.utility;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import com.n26.constants.ExceptionConstants;
import com.n26.exceptions.EntityNotParsableException;
import com.n26.exceptions.FutureDatedTimeStampException;

public class DateTimeUtils {

	public static long currentTimeInMillis() {
		return Instant.now().toEpochMilli();
	}

	public static boolean isWithinSixtySeconds(String timestamp, long currentTimeInMillis, long timeLimit) {
		long diff = currentTimeInMillis - convertToTimeInMillis(timestamp);
		if (diff < 0) {
			throw new FutureDatedTimeStampException(ExceptionConstants.FUTURE_DATED_TIMESTAMP);
		}
		return diff > timeLimit ? false : true;

	}

	public static long convertToTimeInMillis(String timestamp) {
		Instant instant;
		try {
			instant = Instant.parse(timestamp);
			return instant.toEpochMilli();
		} catch (DateTimeParseException dtp) {
			throw new EntityNotParsableException(ExceptionConstants.UNPARSABLE_ENTITY + "timestamp");
		}
	}

}
