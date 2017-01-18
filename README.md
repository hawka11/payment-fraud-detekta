setup neo4j
```
cd environment
vagrant up
./run.sh
```

load initial data under project payment-ingester
```
execute main in detekta.payment.ingester.LoadInitialNeo4jData
```

open neo4j console
```
http://10.0.10.50:7474/browser/
```

login with 
```
neo4j/fraud
```