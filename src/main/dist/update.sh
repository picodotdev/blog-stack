#!/bin/bash
cd $OPENSHIFT_DATA_DIR/blogstack/

./main.sh -e production -index -generate -share >> misc/logs/server.log 2>&1