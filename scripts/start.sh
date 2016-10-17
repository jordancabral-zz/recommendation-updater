#!/bin/bash

export JMX_PORT="22222"

$(dirname $0)/run-class.sh kafka.consumer.SimpleKafkaConsumer $@
PID=$!
echo $PID > pid.txt
