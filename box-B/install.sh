#!/bin/bash

software_dir=/cm/shared/apps/u24_software/pipeline_bwang

if [ ! -d "$software_dir" ]; then
  echo "Making directory $software_dir"
  mkdir -p $software_dir
fi

mv kumquat $software_dir
mv start_node_server.sh $software_dir
mv server.js $software_dir

logs_dir=$software_dir/logs
if [ ! -d "$logs_dir" ]; then
  echo "Making directory $logs_dir"
  mkdir -p $logs_dir
  mkdir -p $logs_dir/njs # nodejs logs
  mkdir -p $logs_dir/pbs # torque logs
fi

# Compile
cd $software_dir/kumquat
./compile.sh

echo "Done"
