source ./setenv.sh
echo "Removing shutdown file:"  $PDFGENSHUTDOWNFILE
rm -f $PDFGENSHUTDOWNFILE
java -Dpdfgenbatch.batchId=9999 -Dpdfgenbatch.batchMode=BATCHMODE -Dformpriority.min=0 -Dformpriority.max=1 -Dlog4j.configuration=file:./config/log4j.properties -server -Xms4096M -XX:+UseParallelGC -jar ../bin/pdfgen-1.1.1-SNAPSHOT.jar &
