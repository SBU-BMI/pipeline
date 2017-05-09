<!-- @author tdiprima -->
# WSI Segmentation & Data Loading Pipeline

## Install
Use the `box-A/install.sh` and `box-B/install.sh` scripts to install the scripts on box-A and box-B, respectively.

Execute `start_node_server.sh` to start the node.js server on box-B.  (Requires [node.js](https://nodejs.org) and [forever](https://www.npmjs.com/package/forever).)

You will also need a subfolder (under pipeline) `analysis-package` containing [pathomics_analysis](https://github.com/SBU-BMI/pathomics_analysis) and [pathomics_featuredb](https://github.com/SBU-BMI/pathomics_featuredb).  Need to compile "analysis" as C++ and "featuredb" with Gradle.

### Build pathomics_analysis

```
git clone -b develop https://github.com/SBU-BMI/pathomics_analysis.git
cd pathomics_analysis/nucleusSegmentation
mkdir build && cd build
ccmake ../src
configure, configure, generate
make -j4
```

### Build pathomics_featuredb

```
git clone https://github.com/SBU-BMI/pathomics_featuredb.git
cd pathomics_featuredb/src
gradle installDist
```

## Run
Log on to box-A, and run command `segmentwsi`<br>
It will prompt you for information<br>
And make an HTTP call to the node.js server on box-B.

When job is complete, you will receive an email.

### Look at results
Use caMicrosope (running on box-A) to look at your results
(use the magnifying glass, and click the execution id that you entered in `segmentwsi`).

### Log files
Log files are written here `/cm/shared/apps/u24_software/pipeline/logs`
