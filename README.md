# Transactions-Statistics-aggregator

IMPLEMENTATION APPROACH -----
* I have used ConcurrentHashMap for in-memory thread-safe storage purposes.
* Managing duplicate key issue - 
   If more than one transaction is added in same time(same milliseconds as well), then the key will be appended with zeros depending upon the number of transaction, so that all entries can be made with maintaining correct count. Similarly, at the time of cleanup, the key length is checked first, if its more than 13(Length of time digits in millis), then i have take substring of the key to find the exact time and if it is to be removed from data store then the original key is used for removal. PSB the logic.
   
   public void addTransaction(Long time, BigDecimal value) {

		   LOGGER.info("Adding Transaction ::  timestamp:" + time + "  amount:" + value);
		   BigDecimal res =  statsStorePerSecond.put(time, value);
	   	int multiple = 100;
		   while(res != null) {
			   res = statsStorePerSecond.put(time * multiple, res);
			   multiple *= 10;
		   }
	   }
    
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
    
* There is a async job scheduled which will be trigerred evry millisecond which will check if any transaction is expired. If yes, the entry will be removed, statistics will be aggregated and it will be updated so that the get operation can fetch the data in constant time O(1). 
* To support the async and parallel processing, there is a bean defined which creates a TaskExecutor, taking the corePoolSize and maxPoolSize from the properties file.
* For statistics aggregation, Java provides a class java.util.DoubleSummaryStatistics which is a state object for collecting statistics such as count, min, max, sum, and average for the statistics. It was obvious that this class will not work for BigDecimal, so i created another class named BigDecimalSummaryStatistics, which will implement Consumer(I) and will accept the BigDecimal objects and will further recalculate the aggregates and save the state, so that the get operation can be don in constant time.
* Rest all implementations are as usual simple java implementations. I have tried to provide documentation for many methods and classes.




#Requirements -----
 

#### RESTful API for statistics. The main use case for the API is to calculate real time statistics for the last 60 seconds of transactions.

The API needs the following endpoints:
*	*POST /transactions – called every time a transaction is made.*
*	*GET /statistics – returns the statistic based of the transactions of the last 60 seconds.*
*	*DELETE /transactions – deletes all transactions.*
 
### Specs
#### POST: /transactions
This endpoint is called to create a new transaction.
```
Body:
{
  "amount": "12.3343",
  "timestamp": "2018-07-17T09:59:51.312Z"
}
```
Where:
*	*amount – transaction amount; a string of arbitrary length that is parsable as a BigDecimal*
*	*timestamp – transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the UTC timezone *
 
Returns: Empty body with one of the following:
*	*201 – in case of success*
*	*204 – if the transaction is older than 60 seconds*
*	*400 – if the JSON is invalid*
*	*422 – if any of the fields are not parsable or the transaction date is in the future*
 
#### GET: /statistics
This endpoint returns the statistics based on the transactions that happened in the last 60 seconds. It MUST execute in constant time and memory (O(1)).

Returns:
{
  "sum": "1000.00",
  "avg": "100.53",
  "max": "200000.49",
  "min": "50.23",
  "count": 10
}
 
Where:
*	*sum – a BigDecimal specifying the total sum of transaction value in the last 60 seconds*
*	*avg – a BigDecimal specifying the average amount of transaction value in the last 60 seconds*
*	*max – a BigDecimal specifying single highest transaction value in the last 60 seconds*
*	*min – a BigDecimal specifying single lowest transaction value in the last 60 seconds*
* *count – a long specifying the total number of transactions that happened in the last 60 seconds*

All BigDecimal values always contain exactly two decimal places and use `HALF_ROUND_UP` rounding. eg: 10.345 is returned as 10.35, 10.8 is returned as 10.80
 
#### DELETE: /transactions
This endpoint causes all existing transactions to be deleted.
The endpoint should accept an empty request body and return a 204 status code.
 
## SetUp Application
To Build: (Unit Test cases included)
mvn clean install 

To Run Integration tests:
mvn clean integration-test


### Time Complexity
* ConcurrentHashMap used to achieve thread-safe & concurrent transactions from API requests. GET operation performed in constant time O(1).


