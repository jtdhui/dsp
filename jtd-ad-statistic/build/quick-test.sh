#!/bin/sh
cd ..
mvn clean package -P assembly-quick,assembly,test
