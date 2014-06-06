docker-storm
=============
A Dockerfile for deploying a [Storm](http://storm.incubator.apache.org/) cluster under [supervision](http://supervisord.org/) using [Docker](https://www.docker.io/)
 containers. 

The image is registered to the [Docker Index](https://index.docker.io/u/fhuz/docker-storm/)

Inspired by [https://github.com/wurstmeister/storm-docker](https://github.com/wurstmeister/storm-docker)

Installation
------------
1. Install [Docker](https://www.docker.io/)
2. Pull the Docker image : ```docker pull fhuz/docker-storm```

Usage
-----
The image contains an **ENTRYPOINT** for running one container per storm daemon as follow:
  
```docker run [OPTIONS] --link zookeeper:zk -d fhuz/docker-storm --daemon (nimbus, drpc, supevisor, ui, logviewer)```  

For instance to run a Nimbus :

```
docker run \  
      --name storm-nimbus -h nimbus \  
      --expose 6627 --expose 3772 --expose 3773 \  
      --link zookeeper:zk \  
      -d fhuz/docker-storm \  
      --daemon nimbus
```

Or you can checkout this minimal **[Makefile](https://github.com/fhussonnois/docker-storm/blob/master/Makefile)** for directly building and deploying storm.

To rebuild the **fhuz/docker-storm** image just run :

  - ```make storm-build```

Run the following commands to deploy/destroy your cluster.

  - ```make deploy-cluster```
  - ```make destroy-cluster```

Finally to sumbit a topology (without storm installed on your machine) :  
```
docker run --rm --entrypoint storm  \  
       -v <HOST_TOPOLOGY_TARGET_DIR>:/home/storm fhuz/docker-storm \   
       -c nimbus.host=`docker inspect storm-nimbus | grep IPAddress | cut -d '"' -f 4)` jar <TOPOLOGY_JAR <TOPOLOGY_ARGS>
```

Port binding 
-------------

Storm UI/Logviewer container ports are exposed to the host system : 

  - Storm UI : [http://localhost:49002/](http://localhost:49002/)
  - Logviewer : [http://localhost:49003/](http://localhost:49003/)
