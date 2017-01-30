#!/bin/bash

vagrant destroy -f
vagrant up
./provision.sh
