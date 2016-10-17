#!/bin/bash

if [ $# -lt 1 ];
then
  echo "USAGE: $0 classname [opts]"
  exit 1
fi

base_dir=$(dirname $0)/..

CLASSPATH=$base_dir/conf

for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

if [ -z "$JMX_OPTS" ]; then
  JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false "
fi

if [ -z "$JVM_OPTS" ]; then
  JVM_OPTS="-Xmx512M -Dlog4j.configuration=file:$base_dir/conf/log4j.properties -Dkafka.consumer.configuration=$base_dir/conf/kafka_consumer.properties -Dmongodb.configuration=$base_dir/conf/mongodb.properties"
fi

if [  $JMX_PORT ]; then
  JMX_OPTS="$JMX_OPTS -Dcom.sun.management.jmxremote.port=$JMX_PORT "
fi

if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi

$JAVA $JVM_OPTS $JMX_OPTS -cp $CLASSPATH $@ &
