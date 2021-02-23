#!/bin/sh

SR="$(for idx in `seq 1 6`; do \
  echo -n "$(docker inspect -f '{{(index .NetworkSettings.Networks "playground_default").IPAddress}}' playground_redis-node${idx}_1):6379 "; \
done;)"
echo "yes" | redis-cli --cluster create $SR --cluster-replicas $2 --cluster-yes