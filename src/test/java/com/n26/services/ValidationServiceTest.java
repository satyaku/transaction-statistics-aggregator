package com.n26.services;

import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.exceptions.BadRequestException;
import com.n26.exceptions.EntityNotParsableException;
import com.n26.models.AddTransactionRequest;
import com.n26.services.interfaces.IAddTransactionInputValidationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationServiceTest {

	@Autowired
	private IAddTransactionInputValidationService addTransactionInputValidationService;

	@Test
	public void testValidateAddTransactionRequestPositive() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("963.2587");
		request.setTimestamp(Instant.now().toString());
		assertTrue(addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli()));
	}

	@Test(expected = BadRequestException.class)
	public void testValidateAddTransactionRequestNullInput() {
		AddTransactionRequest request = null;
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}

	@Test(expected = BadRequestException.class)
	public void testValidateAddTransactionRequestNullAmount() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount(null);
		request.setTimestamp("2021-04-10T16:14:41.171Z");
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}

	@Test(expected = BadRequestException.class)
	public void testValidateAddTransactionRequestEmptyAmount() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("");
		request.setTimestamp("2021-04-10T16:14:41.171Z");
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}

	@Test(expected = BadRequestException.class)
	public void testValidateAddTransactionRequestNullTimeStamp() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("852.3698");
		request.setTimestamp(null);
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}

	@Test(expected = BadRequestException.class)
	public void testValidateAddTransactionRequestEmptyTimeStamp() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("963.2587");
		request.setTimestamp("");
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}
	
	@Test(expected = EntityNotParsableException.class)
	public void testValidateAddTransactionRequestBadFormatTimeStamp() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("852.3698");
		request.setTimestamp("16:14:41.171Z");
		addTransactionInputValidationService.validate(request, Instant.now().toEpochMilli());
	}

}
