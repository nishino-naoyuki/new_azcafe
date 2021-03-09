#!/bin/bash

cd $1
java -jar checkstyle-8.40-all.jar -c azcafe.xml -o $2 $1/*.java