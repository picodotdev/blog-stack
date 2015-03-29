#!/bin/bash
if [ -n "$OPENSHIFT_DATA_DIR" ]; then
        export GIT_SSH=$OPENSHIFT_DATA_DIR/blogstack/git.sh $@

        eval "$(ssh-agent)"
        ssh-add $OPENSHIFT_DATA_DIR/blogstack/.ssh/openshift
fi

rm -rf _deploy/*
cp -a _public/* _deploy/
cp misc/files/robots.txt _deploy/
cd _deploy
git pull origin gh-pages
git add -A .
git commit -m "Site updated $(date +"%A, %d-%m-%Y %R")"
git push origin gh-pages

if [ -n "$OPENSHIFT_DATA_DIR" ]; then
        ssh-agent -k
fi
cd ..

sleep 10s
./main.sh -e production -share -newsletter >> misc/logs/server.log 2>&1