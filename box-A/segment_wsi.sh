#!/bin/bash
# THIS SCRIPT PROMPTS USER FOR INPUT
# DOES ERROR CHECKING
# AND SENDS REQUEST VIA HTTP

echo ""
echo "I'm going to ask you for some information."
echo "Ready?"
echo ""

CORRECT=false     # The correct flag

#host=localhost
# quip-data container ip
host=172.18.0.2
port=27017
dbname=""
caseid=""
execid=""

#echo -n "Enter your email address > "
#read email

# Check database
while [ "$CORRECT" != "true" ]
do
        # Ask the user for the database...
        echo -n "Enter database name (eg. u24_lgg, u24_luad, u24_gbm, u24_brca) > "
        read dbname

        # Validate the input...
        return_str=$(mongo --eval "connect('$host:$port/$dbname').images.findOne({})" | grep "_id" | xargs)
        #echo "connect('$host:$port/$dbname').images.findOne({})"
        #echo $return_str
        if [ "$return_str" = "" ]; then
            echo "Database name incorrect: $dbname"
        else
            CORRECT=true
        fi
done


# Check case id
CORRECT=false     # Reset the flag
while [ "$CORRECT" != "true" ]
do
        # Ask the user for the caseid...
        echo -n "Enter case id > "
        read caseid

        # Validate the input...
        return_str=$(mongo --eval "connect('$host:$port/$dbname').images.find({"case_id" : '$caseid'}).limit(1).pretty()" | grep case_id | xargs)
        if [ "$return_str" = "" ]; then
            echo "Case ID incorrect: $caseid"
            echo "Hint!  Did you enter the correct database for this Case ID?"
        else
            CORRECT=true
        fi
done

# Check execution id
CORRECT=false     # Reset the flag
while [ "$CORRECT" != "true" ]
do
        # Ask the user for the caseid...
        echo -n "Enter execution id > "
        read execid

        return_str=$(mongo --eval "connect('$host:$port/$dbname').metadata.find({'provenance.analysis_execution_id':'$execid','image.case_id':'$caseid'}).pretty()" | grep analysis_execution_id | xargs)
        size=${#return_str}

        # Validate the input...
        if [[ $size -gt 0 ]];then
            echo "Execution ID $execid already used."
        else
            CORRECT=true
        fi
done

echo ""
echo "For this next part, enter a value, or blank for default."
echo ""

echo -n "Enter otsu ratio (threshold grain) > "
read otsuRatio

echo -n "Enter curvature weight (expected round/smoothness) > "
read curvatureWeight

echo -n "Enter size lower threshold > "
read sizeLowerThld

echo -n "Enter size upper threshold > "
read sizeUpperThld

echo -n "Enter size kernel size > "
read msKernel

echo -n "Enter microns per pixel > "
read mpp

CMD="curl http://eagle.bmi.stonybrook.edu:5001?"

if [ "$caseid" ];then
   CMD+="c=$caseid"
fi
if [ "$execid" ];then
   CMD+="&a=$execid"
fi

if [ "$dbname" ];then
   CMD+="&o=$dbname"
fi

if [ "$otsuRatio" ];then
   CMD+="&r=$otsuRatio"
fi

if [ "$curvatureWeight" ];then
   CMD+="&w=$curvatureWeight"
fi

if [ "$sizeLowerThld" ];then
   CMD+="&l=$sizeLowerThld"
fi

if [ "$sizeUpperThld" ];then
   CMD+="&u=$sizeUpperThld"
fi

if [ "$msKernel" ];then
   CMD+="&k=$msKernel"
fi

if [ "$mpp" ];then
   CMD+="&m=$mpp"
fi

if [ "$email" ];then
   CMD+="&e=$email"
fi

echo $CMD

$CMD
