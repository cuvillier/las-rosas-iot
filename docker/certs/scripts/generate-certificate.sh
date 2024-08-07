#!/bin/bash
cd `dirname "$0"`/..

source ../../scripts/setenv.sh

if [ "$#" -eq  "0" ]
then
   echo "Usage: generate-certificate.sh <profile>"
   exit 1
fi

PLATFORM=$1

if [ ! -d "$PLATFORM" ]; then
  echo "Profile $PLATFORM does not exists"
  exit 1;
fi

cd "$PLATFORM" || exit

#
# Clean up first
rm -f "$PLATFORM".*
rm -f "$PLATFORM"-truststore.jks
rm -f Root*

echo PLATFORM="$PLATFORM"

echo 'Generate platform private and public keys'
openssl req -new -nodes -newkey rsa:2048 -keyout "$PLATFORM".key -out "$PLATFORM".csr -subj "/C=ES/ST=CADIZ/L=VILLAMARTIN/O=LASROSAS/CN=*.las-rosas.es"

echo 'Generate platform certificate signed with LasRosasRootCA private key with the AltNames'
openssl x509 -req -sha256 -days 99999 -in "$PLATFORM".csr -CA ../rootCA/LasRosasRootCA.pem -passin pass:changeit -CAkey ../rootCA/LasRosasRootCA.key -CAcreateserial -extfile domains.ext -out "$PLATFORM".crt

echo 'Generate platform JKS truststore including the platform certificate'
keytool -importcert  -alias "$PLATFORM" -noprompt -file "$PLATFORM".crt -keystore "$PLATFORM"-truststore.jks -storepass changeit -srcstorepass changeit

echo 'Generate cacerts trustustore including the plaform certificate. Used by FO'

# cacerts-from-jre is a copy of the $JAVA_HOME/jre/lib/security/cacerts file in fo-server container
cp ../scripts/cacerts-from-jre cacerts
keytool -import -trustcacerts -keystore cacerts  -storepass changeit -noprompt -alias "$PLATFORM".crt -file "$PLATFORM".crt
