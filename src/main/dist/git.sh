#!/bin/bash
# Archivo de configuración de git para OpenShift
ssh -o UserKnownHostsFile=$OPENSHIFT_DATA_DIR/blogstack/.ssh/known_hosts $@