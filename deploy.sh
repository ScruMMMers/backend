#!/bin/bash

docker pull ggnathion/internship-app:latest
docker stop internship-app || true
docker rm internship-app || true
docker run -d --name internship-app -p 8080:8080 ggnathion/internship-app:latest