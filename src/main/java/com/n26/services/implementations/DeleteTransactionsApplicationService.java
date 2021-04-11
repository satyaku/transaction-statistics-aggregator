package com.n26.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.n26.repository.interfaces.ITransactionRepository;
import com.n26.services.interfaces.IDeleteTransactionsApplicationService;

/**
 * 
 * This class contains the logic for interacting with repository 
 * for deleting all the transactions from the data store.
 * 
 * @author Satyam Kumar
 *
 */
@Service
public class DeleteTransactionsApplicationService implements IDeleteTransactionsApplicationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTransactionsApplicationService.class);
	
	@Autowired
	private ITransactionRepository transactionRepository;

	@Override
	public ResponseEntity<Void> deleteTransations() {
		
		LOGGER.debug("Entered in DeleteTransactionsApplicationService ** ");
		boolean status = transactionRepository.clearDataStore();
		LOGGER.debug("Response status : {}",status);
		return status == true ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
