#!/bin/bash

docker pull ggnathion/internship-app:latest
docker stop internship-app || true
docker rm internship-app || true
APP_VERSION=latest docker compose up internship-app -d
