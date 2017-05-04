#!/bin/bash

DIRECTORY=/cm/shared/apps/u24_software/pipeline_bwang

if [ ! -d "$DIRECTORY" ]; then
  echo "Making directory $DIRECTORY"
  mkdir -p $DIRECTORY
fi

mv kumquat $DIRECTORY
mv start_node_server.sh $DIRECTORY
mv server.js $DIRECTORY

DIRECTORY2=$DIRECTORY/logs
if [ ! -d "$DIRECTORY2" ]; then
  echo "Making directory $DIRECTORY2"
  mkdir -p $DIRECTORY2
  mkdir -p $DIRECTORY2/njs
  mkdir -p $DIRECTORY2/pbs
fi

cd $DIRECTORY/kumquat
./compile.sh

echo "Done"
