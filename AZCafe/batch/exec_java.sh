#!/bin/bash

cd $1/classes
java $2 > $1/$3

# PIDを取得
pid=$!

# 100ms待つ
usleep 100000
time=0

while true
do
#生存確認
isAlive=`ps -ef | grep "$pid" | grep -v grep | grep -v srvchk | wc -l`

echo $isAlive
echo $time

if [ $isAlive = 1 ]; then
  if [ $time = ${10} ]; then
    #タイムアウトに達したのでエラーにする
    kill $pid
    exit 1
  fi
else
  exit 0
fi
# 1秒待つ
usleep 1000000
time=$(( time + 1 ))
done