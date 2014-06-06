docker-storm
=============
A Dockerfile for deploying a [Storm](http://storm.incubator.apache.org/) cluster under [supervision](http://supervisord.org/) using [Docker](https://www.docker.io/)
 containers. 

Inspired by [https://github.com/wurstmeister/storm-docker](https://github.com/wurstmeister/storm-docker)

Installation
------------
1. Install [Docker](https://www.docker.io/)

Usage
-----
This project uses a minimal **Makefile** for building and deploying storm.

To rebuild the **ubuntu/storm** image just run :

  - ```make storm-build```

Run the following commands to deploy/destroy your cluster.


  - ```make deploy-cluster```
  - ```make destroy-cluster```

Finally to sumbit a topology (without storm installed on your machine) :
```
docker run --rm --entrypoint storm/ubuntu -v <HOST_TOPOLOGY_TARGET_DIR>:/home/storm -c nimbus.host=`docker inspect storm-nimbus | grep IPAddress | cut -d '"' -f 4)` jar <TOPOLOGY_JAR> <TOPOLOGY_ARGS>
```

Port binding 
-------------

Storm UI/Logviewer container ports are exposed to the host system : 


  - Storm UI : [http://localhost:49002/](http://localhost:49002/)
  - Logviewer : [http://localhost:49003/](http://localhost:49003/)

