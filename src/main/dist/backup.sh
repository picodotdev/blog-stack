#!/bin/bash
if [ -n "$OPENSHIFT_DATA_DIR" ]; then
	DIR=$OPENSHIFT_DATA_DIR/blogstack/misc/database
else
	DIR=misc/database
fi

DATE=$(date +"%Y-%m-%d")
cp $DIR/app.h2.db $DIR/app.h2.db\ \($DATE\)
zip $DIR/app.h2.db.backup.zip $DIR/app.h2.db\ \($DATE\)
rm $DIR/app.h2.db\ \($DATE\)