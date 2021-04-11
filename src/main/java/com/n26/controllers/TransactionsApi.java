package com.n26.controllers;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.models.AddTransactionRequest;
import com.n26.services.interfaces.IAddTransactionApplicationService;
import com.n26.services.interfaces.IDeleteTransactionsApplicationService;

/**
 * Controller for write APIs
 * 
 * @author Satyam Kumar
 */

@RestController
public class TransactionsApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsApi.class);
	
	@Autowired
	private IDeleteTransactionsApplicationService deleteTransactionsApplicationService;

	@Autowired
	private IAddTransactionApplicationService addTransactionApplicationService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/transactions")
	public ResponseEntity<Void> deleteTransactions() {
		
		LOGGER.debug("DELETE /transactions invoked ");
		return deleteTransactionsApplicationService.deleteTransations();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/transactions")
	public ResponseEntity<Void> addTransaction(@RequestBody AddTransactionRequest addTransactionRequest) {
		
		LOGGER.debug("POST /transactions invoked with input : {}",addTransactionRequest);
		return addTransactionApplicationService.addTransaction(addTransactionRequest,
				Instant.now().toEpochMilli());
	}

}
