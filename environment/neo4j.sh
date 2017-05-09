#!/bin/bash

vagrant destroy -f
vagrant up
ansible-playbook  --inventory-file=./provision/inv_default -u vagrant --private-key=~/.vagrant.d/insecure_private_key ./provision/site.yml --tags docker,neo4j