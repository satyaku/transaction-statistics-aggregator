package com.n26.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.n26.repository.interfaces.ITransactionRepository;

/**
 * This class contains a async scheduled job
 * Runs async and clears the outdated transactions from the data store
 * Triggered in every millisecond
 * Also updates the aggregated statistics
 * 
 * @author Satyam Kumar
 *
 */
@Component
public class DataStoreCleanUpUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataStoreCleanUpUtils.class);
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Autowired
	private ITransactionRepository transactionRepository;
	
	@Async
	@Scheduled(fixedDelay = 1, initialDelay = 0)
	public synchronized void cleanUpTaskAtFixedRate() {
		
	    LOGGER.debug("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
	    transactionRepository.removeOutdatedDataFromStore(Instant.now().toEpochMilli());
	    transactionRepository.updateStatistics();
	}
}
