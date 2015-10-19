#!/usr/bin/env bash

# Enter posix mode for bash
set -o posix
set -e

usage="Usage: startup.sh [--daemon (nimbus|drpc|supervisor|ui|logviewer]"

if [ $# -lt 1 ]; then
 echo $usage >&2;
 exit 2;
fi

daemons=(nimbus, drpc, supervisor, ui, logviewer)

# Create supervisor configurations for Storm daemons
create_supervisor_conf () {
    echo "Create supervisord configuration for storm daemon $1"
    cat /home/storm/storm-daemon.conf | sed s,%daemon%,$1,g | tee -a /etc/supervisor/conf.d/storm-$1.conf
}

# Command
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
        exit 1;
    ;;
esac

# Set nimbus address to localhost by default
if [ -z "$NIMBUS_ADDR" ]; then
  export NIMBUS_ADDR=127.0.0.1;
fi

# Set zookeeper address to localhost by default
if [ -z "$ZOOKEEPER_ADDR" ]; then
  export ZOOKEEPER_ADDR=127.0.0.1;
fi

# Set storm UI port to 8080 by default
if [ -z "$UI_PORT" ]; then
  export UI_PORT=8080;
fi

# storm.yaml - replace zookeeper and nimbus ports with environment variables exposed by Docker container(see docker run --link name:alias)
if [ ! -z "$NIMBUS_PORT_6627_TCP_ADDR" ]; then
  export NIMBUS_ADDR=$NIMBUS_PORT_6627_TCP_ADDR;
fi

if [ ! -z "$ZK_PORT_2181_TCP_ADDR" ]; then
  export ZOOKEEPER_ADDR=$ZK_PORT_2181_TCP_ADDR;
fi

function init_storm_yaml() {
    STORM_YAML=$STORM_HOME/conf/storm.yaml
    cp $STORM_HOME/conf/storm.yaml.template $STORM_YAML

    sed -i s/%zookeeper%/$ZOOKEEPER_ADDR/g $STORM_YAML
    sed -i s/%nimbus%/$NIMBUS_ADDR/g $STORM_YAML
    sed -i s/%ui_port%/$UI_PORT/g $STORM_YAML
    for var in `( set -o posix ; set ) | grep CONFIG_`; do
        name=${var%"="*}
        confValue=${var#*"="}
        confName=`echo ${name#*CONFIG_} | awk '{print tolower($0)}'`
        confName=${confName/_/\.}
        echo "Update storm.yaml with $confName=$confValue"
        n=`echo $(grep -n "${confName}:" "${STORM_YAML}" | cut -d : -f 1)`
        echo "n=$n"
        if [ ! -z "$n" ]; then
            echo test
           sed -i "${n}s|.*|$confName: $confValue|g" $STORM_YAML
        else
           echo ${STORM_YAML}
           $(echo "$confName: $confValue" >> ${STORM_YAML})
        fi
        echo "Update storm.yaml with $confName: $confValue"
    done
}

init_storm_yaml

supervisord

exit 0;
