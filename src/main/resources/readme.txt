Setup and Running Guide for PDF Generator process
-------------------------------------------------
Ensure disk spcae of at least 1GB, in the installation folder
Unzip the code drop zip file in the directory where application is to be installed.
Ensure that the following are present in the directory:

1. pdfgen-x.y.z-SNAPSHOT.jar - Application jar file that contains main app and its dependencies

2. cacerts - This is the SSL Keystore. The SSL certificate of all servers the application will 
             connect to using SSL(DMS / WSO2) , needs to be added into this certificates file

3. log - This is the folder where all logs will be generated

4. pdf - this is the folder where temporary pdf files will be stored

5. resources - This folder contains the iText license file and template PDF files (XFA converted to PDF)

6. etc - This folder contains scripts to start and stop the application and the environment setup 

Application Setup:

1. The file scripts/setenv.sh needs to be modified to set all parameters for the given environment
2. To start the application: 
    a. goto Application install directory
    b. sh ./etc/start.sh
3. To stop the application:
    a. goto Application install directory
    c. sh ./etc/stop.sh
 



