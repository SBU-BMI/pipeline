#!/bin/bash
# Requires: forever
# Install forever locally via npm, and put in your PATH.

DIR=/cm/shared/apps/u24_software/pipeline_bwang
pgm="server.js"

rm $DIR/logs/njs/*.log
forever -l $DIR/logs/njs/console.log -e $DIR/logs/njs/error.log start $DIR/$pgm
