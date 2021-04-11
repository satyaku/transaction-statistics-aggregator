package com.n26.services.interfaces;

import com.n26.models.AddTransactionRequest;

public interface IAddTransactionInputValidationService {

	boolean validate(AddTransactionRequest addTransactionRequest, long currentTimeInMillis);

}
