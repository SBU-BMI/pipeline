#!/bin/bash

JAVADIR="/cm/shared/apps/u24_software/pipeline/kumquat"

pkg_loc=".:$JAVADIR/bioformats_package.jar:$JAVADIR/loci_tools.jar:$JAVADIR/commons-cli-1.2.jar:"

echo 'Compiling Apples'
javac -classpath $pkg_loc Apples.java

echo 'Compiling Bananas'
javac -classpath $pkg_loc Bananas.java

echo 'Compiling Cherries'
javac -classpath $pkg_loc Cherries.java

echo 'Compiling Dragonfruit'
javac -classpath $pkg_loc Dragonfruit.java

echo 'Compiling Fig'
javac -classpath $pkg_loc Fig.java
