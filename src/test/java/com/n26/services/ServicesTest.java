package com.n26.services;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.exceptions.BadRequestException;
import com.n26.exceptions.EntityNotParsableException;
import com.n26.exceptions.FutureDatedTimeStampException;
import com.n26.models.AddTransactionRequest;
import com.n26.models.StatisticsSummaryDto;
import com.n26.models.StatisticsSummaryResponse;
import com.n26.repository.interfaces.ITransactionRepository;
import com.n26.services.interfaces.IAddTransactionApplicationService;
import com.n26.services.interfaces.IAddTransactionInputValidationService;
import com.n26.services.interfaces.IDeleteTransactionsApplicationService;
import com.n26.services.interfaces.IGetStatisticsSummaryApplicationService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {

	@Autowired
	private IGetStatisticsSummaryApplicationService getStatisticsSummaryApplicationService;

	@Autowired
	private IDeleteTransactionsApplicationService deleteTransactionsApplicationService;

	@Autowired
	private IAddTransactionApplicationService addTransactionApplicationService;

	@MockBean
	private ITransactionRepository transactionRepository;

	@MockBean
	private IAddTransactionInputValidationService addTransactionInputValidationService;

	@Test
	public void testAddTransactionFailedValidation() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("25.2369");
		request.setTimestamp("2021-04-10T16:14:41.171Z");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenReturn(false);

		ResponseEntity<Void> response = addTransactionApplicationService.addTransaction(request,
				Instant.now().toEpochMilli());
		assertNull(response.getBody());
		assertTrue(204 == response.getStatusCodeValue());
	}

	@Test
	public void testAddTransactionPositive() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("25.2369");
		request.setTimestamp("2021-04-10T16:14:41.171Z");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenReturn(true);

		ResponseEntity<Void> response = addTransactionApplicationService.addTransaction(request,
				Instant.now().toEpochMilli());
		assertNull(response.getBody());
		assertTrue(201 == response.getStatusCodeValue());
	}

	@Test(expected = BadRequestException.class)
	public void testAddTransactionBadRequest() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("");
		request.setTimestamp("2021-04-10T16:14:41.171Z");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenThrow(BadRequestException.class);

		addTransactionApplicationService.addTransaction(request, Instant.now().toEpochMilli());
	}

	@Test(expected = EntityNotParsableException.class)
	public void testAddTransactionUnParsableEntity() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("One hundred");
		request.setTimestamp("2021-04-10T16:14:41.171Z");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenThrow(EntityNotParsableException.class);

		addTransactionApplicationService.addTransaction(request, Instant.now().toEpochMilli());
	}

	@Test(expected = EntityNotParsableException.class)
	public void testAddTransactionUnParsableEntityTime() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("One hundred");
		request.setTimestamp("16253sdatyakj");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenThrow(EntityNotParsableException.class);

		addTransactionApplicationService.addTransaction(request, Instant.now().toEpochMilli());
	}

	@Test(expected = FutureDatedTimeStampException.class)
	public void testAddTransactionFutureDatedTimeStamp() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("25.2369");
		request.setTimestamp("2099-04-10T16:14:41.171Z");

		when(addTransactionInputValidationService.validate(Mockito.any(AddTransactionRequest.class), Mockito.anyLong()))
				.thenThrow(FutureDatedTimeStampException.class);

		addTransactionApplicationService.addTransaction(request, Instant.now().toEpochMilli());
	}

	@Test
	public void testDeleteTransactionPositive() {
		ResponseEntity<Void> response = deleteTransactionsApplicationService.deleteTransations();
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteTransactionNegative() {
		when(transactionRepository.clearDataStore()).thenReturn(false);

		ResponseEntity<Void> response = deleteTransactionsApplicationService.deleteTransations();
		assertNull(response.getBody());
		assertTrue(500 == response.getStatusCodeValue());
	}

	@Test
	public void testGetStatisticsSummary() {
		when(transactionRepository.getStatisticsSummary()).thenReturn(getStatisticsSummaryDto());

		ResponseEntity<StatisticsSummaryResponse> response = getStatisticsSummaryApplicationService
				.getStatisticsSummary(Instant.now().toEpochMilli());

		assertNotNull(response.getBody());
		assertTrue(200 == response.getStatusCodeValue());
	}

	private StatisticsSummaryDto getStatisticsSummaryDto() {
		StatisticsSummaryDto dto = new StatisticsSummaryDto();

		dto.setAvg(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		dto.setMin(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		dto.setMax(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		dto.setSum(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		dto.setCount(0L);

		return dto;
	}
	
	@After
	public void destroy() {
		transactionRepository.clearDataStore();
	}

}
