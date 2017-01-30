###setup neo4j
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

###Helpful commands
setup local multicast on mac
```commandline
sudo route -nv add -net 228.0.0.4 -interface <interface>
```

access neo4j docker
```
sudo docker exec -it 2704c54f9288 /bin/bash
```

###notes:
 - velocity checks
 - ip, email, mobile, id, wallet, cc