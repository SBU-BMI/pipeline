## Your folder should contain...

1. **all_images.list** = mapping of images and file location

1. **pathomics_analysis** = segmentation code
	* segmentation program = `mainSegmentFeatures.cxx`
	
	* For the "update" to this project, you would probably **build** a Docker container, and **run** the program via a `docker exec` in `eggplant.pbs` instead of calling `mainSegmentFeatures` directly.
	
		* ### Building & Starting The Image
		
		* Clone the **repo**: `git clone -b develop https://github.com/SBU-BMI/pathomics_analysis.git`
		
		* Navigate to the **test_segmentation** directory: `cd pathomics_analysis/nucleusSegmentation/test_segmentation/`
		
		* Run the `init.sh` script, and it will build the image and start the container for you.

		* `run_docker_segment.py` is a Python script that interfaces with `mainSegmentFeatures`, and it runs the `docker exec` and downloads the resulting zip file for you.
		* For example of how to run it, see "Appendix A" in this document.

3. **pathomics_featuredb** = data loader (loads data to mongodb)
	* For the "update" to this project, you would probably pull the Docker image, and run the program via a `docker exec` in `eggplant.pbs` instead of running `featuredb-loader` via `run_load_wsi.py`.

	<!-- pathomics_featuredb/src/build/install/featuredb-loader/bin/featuredb-loader -->
	
	* ### Docker Image
		* `docker pull sbubmi/pathomics_featuredb`
	
		* See Docker hub for details: [https://hub.docker.com/r/sbubmi/pathomics_featuredb/](https://hub.docker.com/r/sbubmi/pathomics_featuredb/)
	* ### Or just build the code:
		* Clone the **repo**: `git clone https://github.com/SBU-BMI/pathomics_featuredb.git`
		* Navigate to the **src** directory: `cd pathomics_featuredb/src`
		* Install using **gradle**: `gradle installDist`


## Appendix A
### Example - run segmentation

```
python pathomics_analysis/nucleusSegmentation/run_docker_segment.py \
segment \
mycontainer-test_segmentation \
TCGA-41-3393-01Z-00-DX1_18300_68910_600_600_GBM.png \
/path/to/download/the/output/test_out.zip \
-t img \
-j 2 \
-s 12560,47520 \
-b 500,500 \
-d 500,500 \
-a 20170412133151 \
-c TCGA-CS-4938-01Z-00-DX1 \
-p TCGA-CS-4938
```

The `-t img` means you're giving it a patch.

The `-j 2` means you're using Watershed declumping.

* 0 = No declumping
* 1 = Mean shift declumping
* 2 = Watershed declumping

`-s 12560,47520` is the x,y location in the whole slide image (WSI) where this image comes from.

`-b 500,500` and `-b 500,500` are this image's dimensions (500px by 500 px). 

`-a 20170412133151` is the execution ID used for this particular segmentation run.

`-c TCGA-CS-4938-01Z-00-DX1` is the case ID of the WSI.

`-p TCGA-CS-4938` is the subject ID (which is normally the first 12 characters of the case ID, but for non-TCGA images... it might be different.)

<!--
### Pulling & Starting The Image
`docker pull sbubmi/test_segmentation`
`docker run --name $USER-test_segmentation -it -d sbubmi/test_segmentation /bin/bash`		
-->
