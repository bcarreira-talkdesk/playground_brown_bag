#!/bin/sh

mkdir /data/node1 /data/node2 /data/node3
redis-server /usr/local/etc/redis/redis-node1.conf
redis-server /usr/local/etc/redis/redis-node2.conf
redis-server /usr/local/etc/redis/redis-node3.conf

echo "yes" | redis-cli --cluster create 127.0.0.1:16379 127.0.0.1:16380 127.0.0.1:16381 --cluster-yes

sleep infinity