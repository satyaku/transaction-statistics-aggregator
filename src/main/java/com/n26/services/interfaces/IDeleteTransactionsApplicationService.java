package com.n26.services.interfaces;

import org.springframework.http.ResponseEntity;

public interface IDeleteTransactionsApplicationService {

	ResponseEntity<Void> deleteTransations();

}
