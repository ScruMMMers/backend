#!/bin/bash

docker pull ggnathion/app:latest
docker stop my_app || true
docker rm my_app || true
docker run -d --name my_app -p 8080:8080 ggnathion/app:latest