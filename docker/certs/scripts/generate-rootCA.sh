#!/bin/bash
cd `dirname "$0"`/..

echo 'Are you sure to generate rootCA?'
echo 'Enter confirm: yes'

read reply

if [ "$reply" != 'yes' ]
then
  echo 'Bye'
  exit 1
fi
pwd

# Avoid gitbash to replace /C= by C:/C=
export MSYS_NO_PATHCONV=1

echo 'Generate LasRosasRootCA key'
openssl req -x509 -nodes -new -sha256 -days 99999 -newkey rsa:2048 -keyout rootCA/LasRosasRootCA.key -out rootCA/LasRosasRootCA.pem -subj '/O=las-rosas.es/CN=LasRosasRootCA'

echo 'Generate LasRosasRootCA certificate'
openssl x509 -outform pem -in rootCA/LasRosasRootCA.pem -out rootCA/LasRosasRootCA.crt
