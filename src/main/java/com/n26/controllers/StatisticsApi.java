package com.n26.controllers;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.models.StatisticsSummaryResponse;
import com.n26.services.interfaces.IGetStatisticsSummaryApplicationService;

/**
 * Controller for Read APIs
 * 
 * @author Satyam Kumar
 */

@RestController
public class StatisticsApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsApi.class);
	
	@Autowired
	private IGetStatisticsSummaryApplicationService getStatisticsSummaryApplicationService;

	/**
	 * This API is exposed for getting the statistics summary 
	 * for last 60 seconds by default, however, the default can be changed from
	 * the properties file.
	 * 
	 * @return StatisticsSummaryResponse
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/statistics")
	public ResponseEntity<StatisticsSummaryResponse> getStatisticsSummary(){
		
		LOGGER.debug("GET /statistics invoked");
		return getStatisticsSummaryApplicationService.getStatisticsSummary(Instant.now().toEpochMilli());
	}
}
