Out program only reads JSON files for both strategies and portfolios.

This is an example structure of our JSON file for portfolios.

Sample snippet:
{
	"title": "second",                     //Title of the portfolio.
	"transactions": [{                     //List of all transactions.
		"ticker": "MSFT",                    //Stock ticker information.
		"purchaseDate": "2017-04-11T13:40",  //Date of transaction.
		"quantity": 18.8109756097561,        //No of stocks purchased.
		"commission": 10.0,                  //commission fee for this transaction.
		"costPerUnit": 65.6                  //costPerUnit stock on that particular day.
	}, {
		"ticker": "GOOGL",
		"purchaseDate": "2016-04-13T13:30",
		"quantity": 1.6019524606976414,
		"commission": 10.0,
		"costPerUnit": 770.31
	}, {
		"ticker": "AAPL",
		"purchaseDate": "2012-04-11T11:50",
		"quantity": 1.5718327569946557,
		"commission": 0.0,
		"costPerUnit": 636.2
	}]
}

This is an example structure of our JSON file for our strategies.

{
	"strategyName": "test",                    //Name of the strategyName.
	"investmentAmount": 1234.0,                //investmentAmount.
	"commission": 10.0,                        //commission fees for a transaction.
	"tickerWeightsMap": {                      //Map of tickers and weights.
		"T": 100.0                               //ticker id and weight associated with it are key value pairs.
	}
}
