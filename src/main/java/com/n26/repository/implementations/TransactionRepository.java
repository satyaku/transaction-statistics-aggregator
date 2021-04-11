package com.n26.repository.implementations;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.n26.models.StatisticsSummaryDto;
import com.n26.models.TransactionDto;
import com.n26.repository.interfaces.ITransactionRepository;
import com.n26.utility.BigDecimalSummaryStatistics;
import com.n26.utility.BigDecimalUtils;
import com.n26.utility.DateTimeUtils;
import com.n26.utility.ModelMappers;

/**
 * This class contains methods to interact with internal repository.
 * 
 * @author Satyam Kumar
 */

@Repository
public class TransactionRepository implements ITransactionRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepository.class);

	@Value("${config.timeToExpire:60000}")
	private long timeToExpire;

	@Autowired
	private InMemoryDataStore dataStore;

	@Autowired
	private BigDecimalSummaryStatistics statistics;

	/**
	 * Clears the data store and updates the aggregated summary
	 * 
	 * @return boolean
	 */
	@Override
	public boolean clearDataStore() {
		LOGGER.debug("Data store clearance requested at : {}", Instant.now());
		synchronized (TransactionRepository.class) {
			this.statistics.clear();
			return dataStore.clearDataStore();
		}
	}

	/**
	 * Converts the timestamp and amount in long and BigDecimal respectively and
	 * stores them in data base via in-mem storage
	 */
	@Override
	public void addTransaction(TransactionDto dto) {
		long timeInMillis = DateTimeUtils.convertToTimeInMillis(dto.getTimestamp());
		BigDecimal amount = BigDecimalUtils.getBigDecimalValue(dto.getAmount());

		dataStore.addTransaction(timeInMillis, amount);
		updateStatistics();
	}

	/**
	 * Cleans up the data store by removing all the expired transactions
	 * 
	 * @return boolean
	 */
	@Override
	public boolean removeOutdatedDataFromStore(long currentTimeMillis) {
		LOGGER.debug("Expited data clearance requested at : {}", Instant.now());
		synchronized (TransactionRepository.class) {
			boolean res = dataStore.removeTransaction(currentTimeMillis);
			updateStatistics();
			return res;
		}
	}

	/**
	 * Provides the aggregated stats in response for the getter call
	 * 
	 * @return StatisticsSummaryDto
	 */
	@Override
	public StatisticsSummaryDto getStatisticsSummary() {
		synchronized (TransactionRepository.class) {
			return ModelMappers.mapBigDecimalSummaryStatisticsToStatisticsSummaryDto(statistics);
		}
	}

	public void updateStatistics() {
		this.statistics = dataStore.getCurrentStatistics();
	}
}
