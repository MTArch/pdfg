source ./setenv.sh
BATCHID=5001
BATCHMODE=RETRY
PROCEXIST=PROCEXIST`ps -e -opid,args | grep java | grep -v grep | grep pdfgenbatch.batchId | grep $BATCHID | grep $BATCHMODE`
if [ "$PROCEXIST" == "PROCEXIST" ]
then
        echo "Removing shutdown file:"  $PDFGENSHUTDOWNFILE
        rm -f $PDFGENSHUTDOWNFILE
        echo "Starting application with Batch ID:"$BATCHID
        java -Dpdfgenbatch.batchId=$BATCHID -Dpdfgenbatch.batchMode=$BATCHMODE -Dformpriority.min=0 -Dformpriority.max=1 -Dlog4j.configuration=file:./config/log4j.properties -server -Xms4096M -XX:+UseParallelGC -jar ../bin/pdfgen-PROD_08NOV2023_R1.jar &
else
    CMDLINE=`echo $PROCEXIST | sed s/PROCEXIST//g`
    echo "PROCESS ALREADY RUNNING with cmdline:: " $CMDLINE
fi
