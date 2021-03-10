#!/bin/bash

cd $2
java -jar checkstyle-8.40-all.jar -c azcafe.xml -o $1/$3 $1/*.java