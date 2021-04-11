package com.n26.respository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.models.StatisticsSummaryDto;
import com.n26.models.TransactionDto;
import com.n26.repository.interfaces.ITransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

	@Autowired
	private ITransactionRepository transactionRepository;

	@Test
	public void testClearDataStore() {
		assertTrue(transactionRepository.clearDataStore());
	}

	@Test
	public void testAddTransaction() {
		TransactionDto dto = new TransactionDto();
		dto.setAmount("852.3698");
		dto.setTimestamp(Instant.now().toString());

		transactionRepository.addTransaction(dto);
		transactionRepository.clearDataStore();
	}

	@Test
	public void testRemoveOutdatedDataFromStore() {
		addData();
		boolean response = transactionRepository.removeOutdatedDataFromStore(Instant.now().toEpochMilli());
		assertFalse(response);
		transactionRepository.clearDataStore();
	}

	@Test
	public void testGetStatisticsSummary() {
		addData();
		StatisticsSummaryDto dto = transactionRepository.getStatisticsSummary();
		assertNotNull(dto);
		transactionRepository.clearDataStore();
	}

	private void addData() {
		TransactionDto dto = new TransactionDto();
		for (int i = 0; i < 7; i++) {
			dto.setAmount("852.3698");
			dto.setTimestamp(Instant.now().toString());
			transactionRepository.addTransaction(dto);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
