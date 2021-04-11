package com.n26.repository.interfaces;

import com.n26.models.StatisticsSummaryDto;
import com.n26.models.TransactionDto;

public interface ITransactionRepository {

	boolean clearDataStore();

	void addTransaction(TransactionDto dto);

	boolean removeOutdatedDataFromStore(long currentTimeMillis);

	StatisticsSummaryDto getStatisticsSummary();

	void updateStatistics();

}
