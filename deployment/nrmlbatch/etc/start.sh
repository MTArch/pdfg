source ./setenv.sh
BATCHID=1001
BATCHMODE=NORMAL
PROCEXIST=PROCEXIST`ps auxw | grep java | grep -v grep | grep pdfgenbatch.batchId | grep $BATCHID | grep $BATCHMODE`
if [ "$PROCEXIST" == "PROCEXIST" ]
then
    echo "Removing shutdown file:"  $PDFGENSHUTDOWNFILE
    rm -f $PDFGENSHUTDOWNFILE
    echo "Starting application with Batch ID:"$BATCHID
    java -Dpdfgenbatch.batchId=$BATCHID -Dpdfgenbatch.batchMode=$BATCHMODE -Dformpriority.min=0 -Dformpriority.max=1 -Dlog4j.configuration=file:./config/log4j.properties -server -Xms4096M -XX:+UseParallelGC -jar ../bin/pdfgen-PROD_08NOV2023_R1.jar &
else
    echo "PROCESS ALREADY RUNNING with cmdline" $PROCEXIST
fi
