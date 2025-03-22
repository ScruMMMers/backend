#!/bin/bash

docker remove swagger-ui -f || true
docker remove nginx -f || true
git pull
docker compose up -d swagger-ui nginx