#
# A minimal Makefile for deploying a Storm cluster using Docker containers.
#
# Authors: Florian Hussonnois <florian.hussonnois_gmail.com>
#
STORM_IMAGE=storm/ubuntu
STORM_HOME=/home/storm

all:

.PHONY: kill-nimbus kill-supervisor kill-ui kill-zookeeper storm-build run-nimbus run-supervisor run-ui

deploy-cluster: storm-build run-zookeeper run-nimbus run-supervisor run-ui
	docker ps

destroy-cluster: kill-nimbus kill-supervisor kill-ui kill-zookeeper 
	docker ps

storm-build:
	docker build -t $(STORM_IMAGE) .

run-zookeeper:
	docker run -p 2181:2181 -p 2888:2888 -p 3888:3888 -h zookeeper --name zookeeper -d jplock/zookeeper
	
run-nimbus: 
	docker run \
                --name storm-nimbus \
                --expose 6627 --expose 3772 --expose 3773 \
                -p 6627:6627 -p 3772:3772 -p 3773:3773 \
		--link zookeeper:zk \
		-d $(STORM_IMAGE) \
		$(STORM_HOME)/startup.sh nimbus drpc
run-supervisor: 
	docker run \
                --name storm-supervisor \
                --expose 6700 --expose 6701 --expose 6702 --expose 6703 --expose 8000 \
                -p 6700:6700 -p 6701:6701 -p 6702:6702 -p 6703:6703 -p 49003:8000 \
                --link storm-nimbus:nimbus \
		--link zookeeper:zk \
		-d $(STORM_IMAGE) \
		$(STORM_HOME)/startup.sh supervisor logviewer
run-ui: 
	docker run \
		--name storm-ui \
		--expose 8080 \
		-p 49002:8080 \
		--link storm-nimbus:nimbus \
		--link zookeeper:zk \
		-d $(STORM_IMAGE) \
		$(STORM_HOME)/startup.sh ui 
kill-zookeeper:
	docker kill zookeeper && docker rm zookeeper
kill-nimbus:
	docker kill storm-nimbus && docker rm storm-nimbus
kill-supervisor:
	docker kill storm-supervisor && docker rm storm-supervisor
kill-ui:
	docker kill storm-ui && docker rm storm-ui
