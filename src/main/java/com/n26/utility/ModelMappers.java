package com.n26.utility;

import java.math.BigDecimal;

import com.n26.models.AddTransactionRequest;
import com.n26.models.StatisticsSummaryDto;
import com.n26.models.StatisticsSummaryResponse;
import com.n26.models.TransactionDto;

public class ModelMappers {

	public static TransactionDto mapToTransactionDto(AddTransactionRequest request) {

		TransactionDto dto = new TransactionDto();
		dto.setAmount(request.getAmount());
		dto.setTimestamp(request.getTimestamp());
		return dto;
	}

	public static StatisticsSummaryDto mapBigDecimalSummaryStatisticsToStatisticsSummaryDto(
			BigDecimalSummaryStatistics stats) {

		StatisticsSummaryDto dto = new StatisticsSummaryDto();

		dto.setAvg(stats.getAverage());
		dto.setCount(stats.getCount());
		dto.setMax(stats.getMax());
		dto.setMin(stats.getMin());
		dto.setSum(stats.getSum());

		return dto;
	}

	public static StatisticsSummaryResponse mapStatisticsSummaryDtoToResponse(StatisticsSummaryDto dto) {

		StatisticsSummaryResponse response = new StatisticsSummaryResponse();

		response.setAvg(dto.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setCount(dto.getCount());
		response.setMax(dto.getMax().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setMin(dto.getMin().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		response.setSum(dto.getSum().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

		return response;
	}

}
