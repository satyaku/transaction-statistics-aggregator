package com.n26.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.exceptions.BadRequestException;
import com.n26.exceptions.EntityNotParsableException;
import com.n26.exceptions.FutureDatedTimeStampException;
import com.n26.models.AddTransactionRequest;
import com.n26.models.StatisticsSummaryResponse;
import com.n26.services.interfaces.IAddTransactionApplicationService;
import com.n26.services.interfaces.IDeleteTransactionsApplicationService;
import com.n26.services.interfaces.IGetStatisticsSummaryApplicationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllersTest {

	@Autowired
	private StatisticsApi statsApi;

	@Autowired
	private TransactionsApi transactionApi;

	@MockBean
	private IGetStatisticsSummaryApplicationService getStatisticsSummaryApplicationService;

	@MockBean
	private IDeleteTransactionsApplicationService deleteTransactionsApplicationService;

	@MockBean
	private IAddTransactionApplicationService addTransactionApplicationService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetStatisticsSummaryDefault() {
		when(getStatisticsSummaryApplicationService.getStatisticsSummary(Mockito.any(Long.class)))

				.thenReturn(new ResponseEntity<>(getEmptyStatsSummary(), HttpStatus.OK));

		ResponseEntity<StatisticsSummaryResponse> response = statsApi.getStatisticsSummary();

		assertNotNull(response.getBody());
		assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
	}

	@Test
	public void testDeleteTransactionDefault() {
		when(deleteTransactionsApplicationService.deleteTransations())
				.thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

		ResponseEntity<Void> response = transactionApi.deleteTransactions();
		assertTrue(response.getBody() == null);
		assertTrue(204 == response.getStatusCodeValue());
	}

	@Test
	public void testDeleteTransactionNegative() {
		when(deleteTransactionsApplicationService.deleteTransations())
				.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		ResponseEntity<Void> response = transactionApi.deleteTransactions();
		assertTrue(response.getBody() == null);
		assertTrue(500 == response.getStatusCodeValue());
	}

	@Test
	public void testAddTransactionDefault() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("75.0032");
		request.setTimestamp("2021-04-10T16:14:41.171Z");

		when(addTransactionApplicationService.addTransaction(Mockito.any(AddTransactionRequest.class),
				Mockito.any(Long.class)))

						.thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		ResponseEntity<Void> response = transactionApi.addTransaction(request);
		assertTrue(response.getBody() == null);
		assertTrue(HttpStatus.CREATED.equals(response.getStatusCode()));
	}

	@Test
	public void testAddTransactionNoContent() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("76.2356");
		request.setTimestamp("2021-04-09T16:14:41.171Z");

		when(addTransactionApplicationService.addTransaction(Mockito.any(AddTransactionRequest.class),
				Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

		ResponseEntity<Void> response = transactionApi.addTransaction(request);
		assertTrue(response.getBody() == null);
		assertTrue(204 == response.getStatusCodeValue());
	}

	@Test(expected = EntityNotParsableException.class)
	public void testAddTransactionNotParsable() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("One Hundred");
		request.setTimestamp("2021-04-09T16:14:41.171Z");

		when(addTransactionApplicationService.addTransaction(Mockito.any(AddTransactionRequest.class),
				Mockito.any(Long.class))).thenThrow(EntityNotParsableException.class);

		transactionApi.addTransaction(request);
	}

	@Test(expected = FutureDatedTimeStampException.class)
	public void testAddTransactionFutureDatedTransaction() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("852.3698");
		request.setTimestamp("2022-04-09T16:14:41.171Z");

		when(addTransactionApplicationService.addTransaction(Mockito.any(AddTransactionRequest.class),
				Mockito.any(Long.class))).thenThrow(FutureDatedTimeStampException.class);

		transactionApi.addTransaction(request);
	}

	@Test(expected = BadRequestException.class)
	public void testAddTransactionBadRequestTransaction() {
		AddTransactionRequest request = new AddTransactionRequest();
		request.setAmount("");
		request.setTimestamp("2099-04-09T16:14:41.171Z");

		when(addTransactionApplicationService.addTransaction(Mockito.any(AddTransactionRequest.class), Mockito.any(Long.class)))
				.thenThrow(BadRequestException.class);

		transactionApi.addTransaction(request);
	}

	private StatisticsSummaryResponse getEmptyStatsSummary() {

		StatisticsSummaryResponse response = new StatisticsSummaryResponse();

		response.setAvg(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setMin(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setMax(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setSum(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setCount(0L);

		return response;
	}
}
