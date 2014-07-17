#!/bin/bash

#docker build -t="joshjdevl/storm" storm
docker build -t="joshjdevl/storm-nimbus" nimbus
docker build -t="joshjdevl/storm-supervisor" supervisor
docker build -t="joshjdevl/storm-ui" ui
