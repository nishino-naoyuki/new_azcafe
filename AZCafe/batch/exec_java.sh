#!/bin/bash

cd $1/classes
java $2 > $1/$3

# PID���擾
pid=$!

# 100ms�҂�
usleep 100000
time=0

while true
do
#�����m�F
isAlive=`ps -ef | grep "$pid" | grep -v grep | grep -v srvchk | wc -l`

echo $isAlive
echo $time

if [ $isAlive = 1 ]; then
  if [ $time = ${10} ]; then
    #�^�C���A�E�g�ɒB�����̂ŃG���[�ɂ���
    kill $pid
    exit 1
  fi
else
  exit 0
fi
# 1�b�҂�
usleep 1000000
time=$(( time + 1 ))
done