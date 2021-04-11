package com.n26.services.interfaces;

import org.springframework.http.ResponseEntity;

import com.n26.models.AddTransactionRequest;

public interface IAddTransactionApplicationService {

	ResponseEntity<Void> addTransaction(AddTransactionRequest addTransactionRequest, Long currentTimeInMillis);

}
