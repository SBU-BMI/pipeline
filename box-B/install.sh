#!/bin/bash

DIRECTORY=/cm/shared/apps/u24_software/pipeline

if [ ! -d "$DIRECTORY" ]; then
  echo "Making directory $DIRECTORY"
  mkdir -p $DIRECTORY
fi

mv kumquat $DIRECTORY
mv start_node_server.sh $DIRECTORY
mv server.js $DIRECTORY

DIRECTORY=/cm/shared/apps/u24_software/pipeline/logs
if [ ! -d "$DIRECTORY" ]; then
  echo "Making directory $DIRECTORY"
  mkdir -p $DIRECTORY
  mkdir -p $DIRECTORY/njs
  mkdir -p $DIRECTORY/pbs
fi

DIRECTORY=/cm/shared/apps/u24_software/pipeline
cd $DIRECTORY/kumquat
./compile.sh

echo "Done"
