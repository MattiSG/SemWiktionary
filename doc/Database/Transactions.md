Transactions use within the API
===============================

Since one of the API goals is to abstract away the storage methods, we decided it should take responsibility for all transactions.

This means some calls have many levels of [nested transactions](http://docs.neo4j.org/chunked/1.5/transactions-interaction.html). After researching the topic, we found [no mention of performance](http://docs.neo4j.org/chunked/1.5/transactions.html) regarding transactions in Neo4j's documentation. We therefore assumed it to be safe.

If performance issues were to be raised, our intended backup is to overload each transaction-needing method with a counterpart taking a `Transaction` as a parameter. This parameter would not be used as such inside the method, but would be a proof that the caller has actually begun a transaction, therefore allowing us to skip that step.
