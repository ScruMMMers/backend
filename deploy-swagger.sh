#!/bin/bash

docker remove swagger-ui -f || true
docker remove nginx -f || true
docker compose up -d swagger-ui nginx