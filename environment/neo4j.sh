#!/bin/bash

ansible-playbook  --inventory-file=./provision/inv_default -u vagrant --private-key=~/.vagrant.d/insecure_private_key ./provision/site.yml --tags neo4j