#!/usr/bin/env bash
docker-compose -f docker-compose-no-tls.yaml down
docker rm $(docker ps -aq)
CHANNEL_NAME=mychannel docker-compose -f docker-compose-no-tls.yaml up --force-recreate