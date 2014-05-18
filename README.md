docker-storm
=============
A Dockerfile for deploying a Storm cluster under supervision using Docker containers. 

Inspired by [https://github.com/wurstmeister/storm-docker](https://github.com/wurstmeister/storm-docker)

Installation
------------
1. Install [Docker](https://www.docker.io/)

Usage
-----
This project uses a minimal **Makefile** for building and deploying storm.

Run the following commands to deploy/destroy your cluster.


  - ```make deploy-cluster```
  - ```make destroy-cluster```

To rebuild the **ubuntu/storm** image just run :

  - ```make storm-build```

Port binding 
-------------

Storm UI/Logviewer container ports are exposed to the host system : 


  - Storm UI : [http://localhost:49002/](http://localhost:49002/)
  - Logviewer : [http://localhost:49003/](http://localhost:49003/)
