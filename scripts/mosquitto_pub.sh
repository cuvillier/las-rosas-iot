#!/bin/sh
docker exec -it mosquitto /usr/bin/mosquitto_pub -h localhost $*