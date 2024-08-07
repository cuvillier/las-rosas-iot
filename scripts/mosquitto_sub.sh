#!/bin/sh
docker exec -it mosquitto /usr/bin/mosquitto_sub -h localhost -v -d $*