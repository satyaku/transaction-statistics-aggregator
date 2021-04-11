package com.n26.services.interfaces;

import org.springframework.http.ResponseEntity;
import com.n26.models.StatisticsSummaryResponse;

public interface IGetStatisticsSummaryApplicationService {

	ResponseEntity<StatisticsSummaryResponse> getStatisticsSummary(long currentTimeInMillis);

}
