#!/bin/bash

set -e

usage="Usage: startup.sh [--daemon (nimbus|drpc|supervisor|ui|logviewer] --storm.options \"key1: 2\\\nkey2: 3\\\nkey3: \\\"someStringValue\\\"\" \n     where key1, key2, ..., keyN are from https://github.com/apache/storm/blob/master/conf/defaults.yaml \n and any strings in val1...N are escaped with quotes. e.g.  \"worker.childopts: \\\"-Xmx768m\\\"\" "

if [ $# -lt 1 ]; then
 echo -e $usage >&2;
 exit 2;
fi

daemons=(nimbus, drpc, supervisor, ui, logviewer)

# Create supervisor configurations for Storm daemons
create_supervisor_conf () {
    echo "Create supervisord configuration for storm daemon $1"
    cat /home/storm/storm-daemon.conf | sed s,%daemon%,$1,g | tee -a /etc/supervisor/conf.d/storm-$1.conf
}

cp $STORM_HOME/conf/storm.yaml.template $STORM_HOME/conf/storm.yaml

anyDaemonsSpecified=0
processingDaemons=0
processingStormOptions=0

while [[ $# > 0 ]] ; do
	
	if [ "$1" == "--daemon" ]; then
		processingStormOptions=0
		processingDaemons=1
		shift
		continue
	fi
	
	if [ "$1" == "--storm.options" ]; then
		processingStormOptions=1
		processingDaemons=0
		shift
		continue
	fi	
	
	if [ $processingDaemons -eq 1 ] ; then
		anyDaemonsSpecified=1
		create_supervisor_conf $1
	fi
	
	if [ $processingStormOptions -eq 1 ] ; then
		echo -e "Storm Option:"
		echo -e "$1"
	
		echo -e $1 >> $STORM_HOME/conf/storm.yaml
	fi
	
	shift
	
done

if [ $anyDaemonsSpecified -eq 0 ]; then
	echo -e $usage
    exit 1;
fi


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

sed -i s/%zookeeper%/$ZOOKEEPER_ADDR/g $STORM_HOME/conf/storm.yaml
sed -i s/%nimbus%/$NIMBUS_ADDR/g $STORM_HOME/conf/storm.yaml
sed -i s/%ui_port%/$UI_PORT/g $STORM_HOME/conf/storm.yaml

supervisord

exit 0;
