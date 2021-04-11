package com.n26.repository.implementations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.n26.utility.BigDecimalSummaryStatistics;

/**
 * In-memory database for the service,
 * this contains ConcurrentHashMap for storage purposes.
 * 
 * @author Satyam Kumar
 */

@Repository
public class InMemoryDataStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDataStore.class);

	private volatile ConcurrentHashMap<Long, BigDecimal> statsStorePerSecond = new ConcurrentHashMap<>();

	/**
	 * Stores the transaction into data store
	 * Keeps adding untill all the values are not added
	 * 
	 * @param time
	 * @param value
	 */
	public void addTransaction(Long time, BigDecimal value) {

		LOGGER.info("Adding Transaction ::  timestamp:" + time + "  amount:" + value);
		BigDecimal res =  statsStorePerSecond.put(time, value);
		int multiple = 100;
		while(res != null) {
			res = statsStorePerSecond.put(time * multiple, res);
			multiple *= 10;
		}
	}
	
	/**
	 * Removes all the expired transactions from data store
	 * returns true if any data has been deleted
	 * 
	 * @param time
	 * @return boolean
	 */
	public boolean removeTransaction(Long time) {

		int size = statsStorePerSecond.size();
		
		for (Long key : statsStorePerSecond.keySet()) {
			Long orig_key = key;
			if(String.valueOf(key).length() > 13) {
				key = Long.parseLong(String.valueOf(key).substring(0, 13));
			}
			if (time - key > 60000) {
				LOGGER.debug("Removing entry with key : {}, as the entry was found expired.", key);
				statsStorePerSecond.remove(orig_key);
			}
		}
		return size == statsStorePerSecond.size() ? false : true;
	}

	/**
	 * Removes all the data from the data store
	 * returns true when the store is empty
	 * 
	 * @return boolean
	 */
	public boolean clearDataStore() {
		LOGGER.info("**Clearing Data Store**");
		statsStorePerSecond.clear();
		return this.isDataStoreEmpty();
	}

	
	/**
	 * Checks if the data store is empty
	 * 
	 * @return boolean
	 */
	public boolean isDataStoreEmpty() {
		return statsStorePerSecond.isEmpty();
	}

	/**
	 * Returns aggregated statistics from the data store
	 * 
	 * @return BigDecimalSummaryStatistics
	 */
	public BigDecimalSummaryStatistics getCurrentStatistics() {
		LOGGER.debug("**Aggregated stats being fetched at : {}**",Instant.now());
		return statsStorePerSecond.values().parallelStream().collect(Collector.of(BigDecimalSummaryStatistics::new,
				BigDecimalSummaryStatistics::accept, BigDecimalSummaryStatistics::merge));
	}

}
