#!/bin/bash 

mvn clean package;
scp target/lreceiver-jar-with-dependencies.jar liuo@121.40.98.72:~/lreceiver;
ssh liuo@121.40.98.72 <<EOF
cd lreceiver;
./shutdown.sh;
./startup.sh;
EOF
echo 'redeployed.'
