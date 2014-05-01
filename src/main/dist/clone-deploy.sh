#!/bin/bash
# Antes subir la clave privada a OpenShift
if [ -n "$OPENSHIFT_DATA_DIR" ]; then
	export GIT_SSH=$OPENSHIFT_DATA_DIR/blogstack/git.sh $@

	eval "$(ssh-agent)"
	ssh-add $OPENSHIFT_DATA_DIR/blogstack/.ssh/openshift
fi

git clone -b gh-pages git@github.com:picodotdev/blog-stack.git _deploy
cd _deploy
git config user.email "pico.dev@gmail.com"
git config user.name "pico.dev"
git config core.editor "vim"
cd ..