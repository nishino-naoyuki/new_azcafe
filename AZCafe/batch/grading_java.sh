#!/bin/bash

  # コンパイルを行う
  javac -d $1/classes $1/*.java 2>$2/error.txt