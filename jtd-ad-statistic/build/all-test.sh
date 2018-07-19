#!/bin/sh
cd ..
mvn clean package -P assembly-all,assembly,test
