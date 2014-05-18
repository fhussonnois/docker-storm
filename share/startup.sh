#!/bin/bash

if [ $# -lt 1 ]; then
 echo "Missing argument, expected at least one daemon name!" >&2;
 exit 2;
fi

# Create supervisor configurations for Storm daemons 
for daemon in $*; do
  cat /home/storm/storm-supervisor.conf | sed s/\$\{daemon\}/$daemon/g | tee -a /etc/supervisor/conf.d/storm-${daemon}.conf
done

if [ -z "$NIMBUS_PORT_6627_TCP_ADDR" ]; then
  export NIMBUS_PORT_6627_TCP_ADDR=`hostname -i` #Nimbus
fi
# storm.yaml - replace zookeeper and nimbus ports with environment variables exposed by Docker container(see docker run --link name:alias)
sed -i s/\$\{zookeeper\}/$ZK_PORT_2181_TCP_ADDR/g $STORM_HOME/conf/storm.yaml
sed -i s/\$\{nimbus\}/$NIMBUS_PORT_6627_TCP_ADDR/g $STORM_HOME/conf/storm.yaml

supervisord



