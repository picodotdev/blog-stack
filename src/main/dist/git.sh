#!/bin/bash
ssh -o UserKnownHostsFile=$OPENSHIFT_DATA_DIR/blogstack/.ssh/known_hosts $@