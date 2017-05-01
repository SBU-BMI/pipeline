#! /usr/bin/env python

# Because we're using a different mainSegmentFeatures

import sys
import os
import subprocess
import getopt
import random
import glob


def print_help():
    print 'run_upload_wsi [-h|<arguments>]'
    print '   Arguments: '
    print '     -t [wsi|tiles|img]'
    print '     -i <tissue image file>'
    print '     -p <output file/prefix>'
    print '     -m <mpp>'
    print '     -r <otsuRatio> '
    print '     -w <curvatureWeight>'
    print '     -l <sizeLowerThld>'
    print '     -u <sizeUpperThld>'
    print '     -k <msKernel>'
    print '     -n <levelsetNumberOfIterations>'
    print '     -d <tileSize>'
    print '     -s <topLeftX,topLeftY>'
    print '     -b <sizeX,sizeY>'
    print '     -o <database name: u24_lgg, u24_luad, u24_gbm, u24_brca>'
    print '     -j <subject id>'
    print '     -c <case id/image id>'
    print '     -a <analysis execution id>'


def print_help_segment():
    print 'print_help_segment'


def parse_args(argv):
    if len(argv) == 0 or argv[0] == '-h':
        print_help()
        sys.exit(2)

    try:
        opts, args = getopt.getopt(argv, "t:i:p:m:r:w:l:u:k:n:d:s:b:o:j:c:a:")
        print "\nopts list: "
        print opts
    except getopt.GetoptError:
        print_help_segment()
        sys.exit(2)

    seg_opts = ''
    inp_file = ''
    db_name = ''
    subject_id = ''
    case_id = ''
    analysis_id = ''
    results_folder = ''
    parm_names = ''

    for opt, arg in opts:
        if opt in ("-t", "-m", "-r", "-w", "-l", "-u", "-k", "-n", "-d", "-s", "-b"):
            seg_opts = seg_opts + opt + ' ' + arg + ' '
        if opt in "-i":
            seg_opts = seg_opts + opt + ' ' + arg + ' '
            inp_file = arg
        if opt in "-p":
            seg_opts = seg_opts + opt + ' ' + arg + ' '
            results_folder = arg
        if opt in "-o":
            db_name = arg
        if opt in "-j":
            subject_id = arg
        if opt in "-c":
            case_id = arg
        if opt in "-a":
            analysis_id = arg

    if seg_opts == '':
        print 'No segmentation options were provided.'
        sys.exit(2)
    if db_name == '':
        print 'No database name was provided.'
        sys.exit(2)
    if subject_id == '':
        print 'No subject id was provided.'
        sys.exit(2)
    if case_id == '':
        print 'No case id (image id) was provided.'
        sys.exit(2)
    if analysis_id == '':
        print 'No analysis execution id was provided.'
        sys.exit(2)

    for opt, arg in opts:
        if opt in ("-k", "-l", "-m", "-r", "-u", "-w"):
            parm_names = parm_names + "'" + opt + "':" + arg + ","

    if parm_names:
        parm_names = parm_names.replace("-k", "kernelSize")
        parm_names = parm_names.replace("-l", "sizeThld")
        parm_names = parm_names.replace("-m", "mpp")
        parm_names = parm_names.replace("-r", "otsuRatio")
        parm_names = parm_names.replace("-u", "sizeUpperThld")
        parm_names = parm_names.replace("-w", "curvatureWeight")
    else:
        # Defaults
        parm_names = "'curvatureWeight':0.8,'kernelSize':20,'mpp':0.25,'otsuRatio':1,'sizeThld':3,'sizeUpperThld':200,"

    print "parm_names " + parm_names

    return seg_opts, inp_file, results_folder, db_name, subject_id, case_id, analysis_id, parm_names


def execute_loader(results_folder, db_name, subject_id, case_id, analysis_id, parm_names):
    print "results_folder " + results_folder
    try:
        names = ''
        if parm_names:
            names = '"{' + parm_names[:-1] + '}"'

        random.seed()
        rnd_val = random.randrange(1000000)
        tmp_file = '/tmp/file' + str(rnd_val) + '.list'
        f = open(tmp_file, 'w')
        files = glob.glob(results_folder + '/*-features.csv')
        for myFile in files:
            f.write('%s,%s,%s,0,0\n' % (subject_id, case_id, myFile))
        f.close()

        loadExe = "/cm/shared/apps/u24_software/pipeline/analysis-package/pathomics_featuredb/src/build/install/featuredb-loader/bin/featuredb-loader"

        run_cmd = loadExe + " --inptype csv --batchid q1 --fromdb --dbhost 129.49.255.19 --dbport 27017 --dest db"
        run_cmd = run_cmd + " --eid " + analysis_id
        run_cmd = run_cmd + " --inplist " + tmp_file
        run_cmd = run_cmd + " --dbname " + db_name
        run_cmd = run_cmd + " --fromdb "
        run_cmd = run_cmd + " --cid " + case_id
        run_cmd = run_cmd + " --sid " + subject_id
        if names:
            run_cmd = run_cmd + " --eparms " + names
        run_cmd = run_cmd + " --studyid small"
        print '\nfeaturedb-loader: ' + run_cmd
        subprocess.call(run_cmd, shell=True)

        os.remove(tmp_file)
    except:
        print 'Program featuredb-loader terminated'
        exit(1)


def main(argv):
    seg_opts, inp_file, results_folder, db_name, subject_id, case_id, analysis_id, parm_names = parse_args(argv)
    print '\nEXECUTING LOADER'
    execute_loader(results_folder, db_name, subject_id, case_id, analysis_id, parm_names)


if __name__ == '__main__':
    main(sys.argv[1:])
