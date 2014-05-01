#!/bin/bash
# Antes subir la clave privada a OpenShift
if [ -n "$OPENSHIFT_DATA_DIR" ]; then
	export GIT_SSH=$OPENSHIFT_DATA_DIR/blogstack/git.sh $@

	eval "$(ssh-agent)"
	ssh-add $OPENSHIFT_DATA_DIR/blogstack/.ssh/openshift
fi

mkdir _deploy
cd _deploy
git init
git config user.email "pico.dev@gmail.com"
git config user.name "pico.dev"
git config core.editor "vim"
git checkout --orphan gh-pages
echo "A Blog Stack site is coming soon&hellip;" > index.html
git add .
git commit -m "Blog Stack init"
git remote add origin git@github.com:picodotdev/blogstack.git
git push origin gh-pages
cd ..