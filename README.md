# nTier Bank Statement Matching
* Bank statements, which are typically imported, can contain many lines.
* Reference information supplied by bank accounts on electronic bank statements, can be used to quickly automatically reconcile, and in many cases create the transactions in the system as well.
* Each bank account is associated with one or more matching rules.
* Each rule can match part of the description (or narration) on a bank statement line.
* If the description matches a particular line on the bank statement, then:
	* The Business Partner field can be automatically populated on the line.
	* One of the following can be done automatically:
		* A charge can be automatically created for the line.
		* A payment be automatically created for the line.
		* An open payment can be automatically matched, based on the amount.
* These rules can then be run, either for each line, or for the bank statement as a whole.

## Installation
1. Download the latest plugin jar from [the download page]
2. Install the plugin jar via Felix Console

## Author
* nTier Bank Statement Matching by [nTier Software Services](http://www.ntier.co.za)
	* Yogan Naidoo (Author)
	* Neil Gordon (Developer)


