#!/bin/bash

software_dir=/cm/shared/apps/u24_software/pipeline

if [ ! -d "$software_dir" ]; then
  echo "Making software_dir $software_dir"
  mkdir -p $software_dir
fi

mv kumquat $software_dir
mv start_node_server.sh $software_dir
mv server.js $software_dir

software_dir=/cm/shared/apps/u24_software/pipeline/logs
if [ ! -d "$software_dir" ]; then
  echo "Making software_dir $software_dir"
  mkdir -p $software_dir
  mkdir -p $software_dir/njs
  mkdir -p $software_dir/pbs
fi

software_dir=/cm/shared/apps/u24_software/pipeline
cd $software_dir/kumquat
./compile.sh

echo "Done"
