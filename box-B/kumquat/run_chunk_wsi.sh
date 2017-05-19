#!/bin/bash
# Get parameters from osprey and pass them to our chunker.
# Chunker will pass info to segmentation algorithm and data loader.

PROGNAME=$(basename "$0")

error_exit() {
   echo "${PROGNAME}: ${1:-"Error"}" 1>&2
   exit 1
}

while getopts ":c:a:s:f:o:r:w:l:u:k:m:e:j:" myopts; do
    case "${myopts}" in
        c)
            caseId=${OPTARG}
            ;;
        a)
            execId=${OPTARG}
            ;;
        s)
            subjectId=${OPTARG}
            ;;
        f)
            filename=${OPTARG}
            ;;
        o)
            dbName=${OPTARG}
            ;;
        r)
            r=${OPTARG}
            ;;
        w)
            w=${OPTARG}
            ;;
        l)
            l=${OPTARG}
            ;;
        u)
            u=${OPTARG}
            ;;
        k)
            k=${OPTARG}
            ;;
        m)
            m=${OPTARG}
            ;;
        e)
            e=${OPTARG}
            ;;
        j)
            j=${OPTARG}
            ;;
    esac
done
shift $((OPTIND-1))

JAVADIR="/cm/shared/apps/u24_software/pipeline_bwang/kumquat"

cd ${JAVADIR}

CP=".:$JAVADIR/bioformats_package.jar:$JAVADIR/loci_tools.jar:$JAVADIR/commons-cli-1.2.jar:"

SIZE=2048

cmdLine="java -classpath $CP Apples -c $caseId -s $subjectId -a $execId -o $dbName -f $filename"

if [ "$r" ];then
   cmdLine+=" -r $r"
fi

if [ "$j" ];then
   cmdLine+=" -j $j"
fi

if [ "$w" ];then
   cmdLine+=" -w $w"
fi

if [ "$l" ];then
   cmdLine+=" -l $l"
fi

if [ "$u" ];then
   cmdLine+=" -u $u"
fi

if [ "$k" ];then
   cmdLine+=" -k $k"
fi

if [ "$m" ];then
   cmdLine+=" -m $m"
fi

echo $cmdLine

$cmdLine

