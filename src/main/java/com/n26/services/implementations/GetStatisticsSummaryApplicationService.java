package com.n26.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.n26.models.StatisticsSummaryDto;
import com.n26.models.StatisticsSummaryResponse;
import com.n26.repository.interfaces.ITransactionRepository;
import com.n26.services.interfaces.IGetStatisticsSummaryApplicationService;
import com.n26.utility.ModelMappers;

/**
 * 
 * This class contains the logic for interacting with repository to get the
 * aggregated statistics and mapping it to the response type.
 * 
 * @author Satyam Kumar
 *
 */

@Service
public class GetStatisticsSummaryApplicationService implements IGetStatisticsSummaryApplicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetStatisticsSummaryApplicationService.class);
	
	@Autowired
	private ITransactionRepository transactionRepository;

	@Override
	public ResponseEntity<StatisticsSummaryResponse> getStatisticsSummary(long currentTimeInMillis) {
		
		LOGGER.debug("GetStatisticsSummaryApplicationService invoked at : {}",currentTimeInMillis);
		StatisticsSummaryDto dto = transactionRepository.getStatisticsSummary();
		StatisticsSummaryResponse response = ModelMappers.mapStatisticsSummaryDtoToResponse(dto);
		
		LOGGER.debug("GetStatisticsSummaryApplicationService responding with : {}",response);
		return new ResponseEntity<StatisticsSummaryResponse>(response, HttpStatus.OK);
	}

}
