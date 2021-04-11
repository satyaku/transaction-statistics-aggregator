package com.n26.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.n26.models.AddTransactionRequest;
import com.n26.models.TransactionDto;
import com.n26.repository.interfaces.ITransactionRepository;
import com.n26.services.interfaces.IAddTransactionApplicationService;
import com.n26.services.interfaces.IAddTransactionInputValidationService;
import com.n26.utility.ModelMappers;

/**
 * 
 * This class contains the business logic for adding a transaction
 * via repository. This contains the call to the validation service
 *  as well which validated the input request of the transaction.
 * 
 * @author Satyam Kumar
 */
@Service
public class AddTransactionApplicationService implements IAddTransactionApplicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddTransactionApplicationService.class);
	
	@Autowired
	private ITransactionRepository transactionRepository;

	@Autowired
	private IAddTransactionInputValidationService addTransactionInputValidationService;

	@Override
	public ResponseEntity<Void> addTransaction(AddTransactionRequest addTransactionRequest, Long currentTimeInMillis) {
		
		LOGGER.debug("Entered in AddTransactionApplicationService ** ");
		if (!addTransactionInputValidationService.validate(addTransactionRequest, currentTimeInMillis)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		TransactionDto dto = ModelMappers.mapToTransactionDto(addTransactionRequest);
		transactionRepository.addTransaction(dto);	
		
		LOGGER.debug("AddTransactionApplicationService executed Successfully.");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
