export APPHOME='/home/ltimsiva/mcaapp/'
export INSTHOME='/home/ltimsiva/mcaapp/nrmlbatch'

export PDF_TEMP_LOCATION_PATH="$APPHOME/tmp/"

export PDFGENSHUTDOWNFILE="$INSTHOME/shutdown_pdfgen.txt"
export TEMPLATES_BASE_PATH="$INSTHOME/config/templates"
export LICENSE_BASE_PATH="$INSTHOME/config/license"
export PDFGEN_LOG_DIR="$INSTHOME/log/"

export MCA_ORACLE_USERNAME=GENPDF
export MCA_ORACLE_PASSWD='DfHGds$weDF'
export MCA_ORACLE_URL='jdbc:oracle:thin:@172.18.22.141:7033/SBLUAT'

export WSO2_TOKEN_URL='https://172.18.41.135:8243/token'
export WSO2_TOKEN='ME4wUDBtQm1NdGVGcTNZX1c5cjdZRkxQZWswYTpwQmVWd3hzTjdJWnVfcEdKUzk1MFZoUmxjQVlh'
export WSO2_API_PROTOCOL=HTTPS
export WSO2_GRANT_TYPE=password
export WSO2_USERNAME=admin
export WSO2_PASSWORD=admin
export WSO2_SSL_KEYSTORE_FILEPATH=$RESOURCES_BASE_PATH/cacerts
export WSO2_ENDPOINT_IP='https://172.18.253.15:8243'

export DMS_USERNAME=aem_uat_user1
export DMS_PASSWORD='Lti12345#'
export DMS_CABINET_NAME='mcauat'
export DMS_TOKEN_URL="http://172.18.253.19:8080/OmniDocsRestWS/rest/services"
export DMS_SSL_KEYSTORE_FILEPATH=$RESOURCES_BASE_PATH/cacerts
export DMS_TOKEN_REFRESH_INTERVAL_MS=600

export ITEXT_LICENSE_FILE_NAME='iText_core_8.0_license.json;iText_pdfXfa_4.0_license.json'

export MCATHREADMAJORSLEEPTIME=100
export MCATHREADMINORSLEEPTIME=50
export BATCHRECORDSLOCKRESETTIME=1000
export BATCHRETRYCOUNT=3

export PDFGEN_DMS_FOLDERDATACLASSID=64
export PDFGEN_DMS_DOCUMENTDATACLASSID=65

PATH=/home/ltimsiva/jdk-19.0.2/bin:$PATH
