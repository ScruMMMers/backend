#!/bin/bash

docker stop swagger-ui || true
docker compose up -d swagger-ui