#!/bin/bash

echo "MUST BE ROOT!"

DIRECTORY=/usr/local/BMI/pipeline
if [ ! -d "$DIRECTORY" ]; then
  echo "Making directory $DIRECTORY"
  mkdir -p $DIRECTORY
fi

cp segment_wsi.sh $DIRECTORY

file=/usr/local/bin/segmentwsi
if [ ! -f $file ]; then
  echo "Creating symlink $file"
  ln -s $DIRECTORY/segment_wsi.sh $file
fi

echo "Done"
