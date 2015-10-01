docker-storm
=============
A Dockerfile for deploying a [Storm](http://storm.apache.org/) cluster under [supervision](http://supervisord.org/) using [Docker](https://www.docker.io/)
 containers. 

The image is registered to the [Docker Index](https://index.docker.io/u/fhuz/docker-storm/)

Installation
------------
1. Install [Docker](https://www.docker.io/)
2. Pull the Docker image : ```docker pull fhuz/docker-storm```

Usage
-----
**Pre-Requisites:** You must have a running zookeeper instance in order to start any of the storm daemons. 
```
docker run -p 2181:2181 -p 2888:2888 -p 3888:3888 -h zookeeper –-name="zookeeper" -d jplock/zookeeper;
```

The image contains an **ENTRYPOINT** for running one container per storm daemon as follow:
  
```
docker run [OPTIONS] --link zookeeper:zk -d fhuz/docker-storm --daemon (nimbus, drpc, supervisor, ui, logviewer) \
     [--storm.options \"key1: 2\nkey2: 3\nkey3: \"someStringValue\"\"] \
```  

--storm.options is optional. 
key1...N are from [Storm's Config Settings](https://github.com/apache/storm/blob/master/conf/defaults.yaml)
and any strings in val1...N must be escaped with quotes. e.g.  \"worker.childopts: \"-Xmx768m\"\" 
Because the value of this parameter will directly be appended to conf/storm.yaml, each key and value pair should be separated by a \n
This was inspired by a need to specify the memory footprint of a specific Storm Daemon.

For instance to run Nimbus :

```
docker run \  
      --name="storm-nimbus" -h nimbus \  
      --expose 6627 --expose 3772 --expose 3773 \  
      --link zookeeper:zk \  
      -d fhuz/docker-storm \  
      --daemon nimbus
```
Docker Compose
---
**Pre-Requisites:** [Install Compose](https://docs.docker.com/compose/#installation-and-set-up)

[Compose](https://docs.docker.com/compose/) is a tool for defining and running complex applications with Docker.
  
  - To start cluster:
	 
    **zookeeper:** ```docker-compose -p storm -f ./docker-zookeeper.yml up``` (pass the -d flag to run container in background)

    **storm:** ```docker-compose -p storm -f ./docker-storm.yml up``` (pass the -d flag to run container in background)

  - To stop cluster:

    **zookeeper:** ```docker-compose -p storm -f ./docker-zookeeper.yml stop```

    **storm:** ```docker-compose -p storm -f ./docker-storm.yml stop```

Makefiles
---------
Or you can checkout this minimal **[Makefile](https://github.com/fhussonnois/docker-storm/blob/master/Makefile)** for directly building and deploying storm.

To rebuild the **fhuz/docker-storm** image just run :

  - ```make storm-build```

Run the following commands to deploy/destroy your cluster.

  - ```make deploy-cluster```
  - ```make destroy-cluster```


How to submit a topology 
------------------------
Without storm installed on your machine:

```
docker run --rm --entrypoint storm  \  
       -v <HOST_TOPOLOGY_TARGET_DIR>:/home/storm fhuz/docker-storm \   
       -c nimbus.host=`docker inspect --format='{{.NetworkSettings.IPAddress}}' storm-nimbus` jar <TOPOLOGY_JAR> <TOPOLOGY_ARGS>
```

Port binding 
-------------

Storm UI/Logviewer container ports are exposed to the host system : 

  - Storm UI : [http://localhost:8080/](http://localhost:8080/)
  - Logviewer : [http://localhost:8000/](http://localhost:8000/)


Troubleshooting
---------------
If for some reasons you need to debug a container you can use docker exec command: 

Example : ```docker exec -it storm_nimbus_1 /bin/bash```
