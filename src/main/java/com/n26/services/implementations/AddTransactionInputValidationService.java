package com.n26.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.n26.constants.ExceptionConstants;
import com.n26.exceptions.BadRequestException;
import com.n26.models.AddTransactionRequest;
import com.n26.services.interfaces.IAddTransactionInputValidationService;
import com.n26.utility.DateTimeUtils;

/**
 * This class contains the validation logic for the input for 
 * adding a transaction. 
 * 
 * @author Satyam Kumar
 *
 */
@Service
public class AddTransactionInputValidationService implements IAddTransactionInputValidationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddTransactionInputValidationService.class);

	@Value("${config.timeToExpire:60000}")
	private long timeToExpire;
	
	@Override
	public boolean validate(AddTransactionRequest addTransactionRequest, long currentTimeInMillis) {
		
		LOGGER.debug("Entered in AddTransactionInputValidationService ** ");

		if(addTransactionRequest == null) {
			throw new BadRequestException(ExceptionConstants.NULL_INPUT_OBJECT);
		}
		
		if (addTransactionRequest.getAmount() == null || addTransactionRequest.getAmount().isEmpty()) {
			throw new BadRequestException(ExceptionConstants.BAD_INPUT_ATTRIBUTE + "Amount");
		}

		if (addTransactionRequest.getTimestamp() == null || addTransactionRequest.getTimestamp().isEmpty()) {
			throw new BadRequestException(ExceptionConstants.BAD_INPUT_ATTRIBUTE + " Timestamp");
		}

		boolean valid = DateTimeUtils.isWithinSixtySeconds(addTransactionRequest.getTimestamp(), currentTimeInMillis, timeToExpire);
		if (!valid) {
			return false;
		}
		LOGGER.debug("AddTransactionInputValidationService executed Successfully.");
		return true;

	}

}
