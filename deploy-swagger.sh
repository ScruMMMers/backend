#!/bin/bash

docker remove swagger-ui -f || true
docker compose up -d swagger-ui