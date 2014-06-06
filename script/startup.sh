#!/bin/bash

usage="Usage: startup.sh [--daemon (nimbus|drpc|supervisor|ui|logviewer]"

if [ $# -lt 1 ]; then
 echo $usage;
 exit 1;
fi

daemons=(nimbus, drpc, supervisor, ui, logviewer)

# Create supervisor configurations for Storm daemons
create_supervisor_conf () {
    cat /home/storm/storm-supervisor.conf | sed s,%daemon%,$daemon,g | tee -a /etc/supervisor/conf.d/storm-$1.conf
}

# Commands
case $1 in
    --daemon)
        shift
        for daemon in $*; do
          create_supervisor_conf $daemon
        done
    ;;
    --all)
        for daemon in daemons; do
          create_supervisor_conf $daemon
        done
    ;;
    *)
        echo $usage
	exit 1
    ;;
esac

if [ -z "$NIMBUS_PORT_6627_TCP_ADDR" ]; then
  export NIMBUS_PORT_6627_TCP_ADDR=`hostname -i` #Nimbus
fi
# storm.yaml - replace zookeeper and nimbus ports with environment variables exposed by Docker container(see docker run --link name:alias)
sed -i s/%ZOOKEEPER%/$ZK_PORT_2181_TCP_ADDR/g $STORM_HOME/conf/storm.yaml
sed -i s/%NIMBUS%/$NIMBUS_PORT_6627_TCP_ADDR/g $STORM_HOME/conf/storm.yaml

supervisord



