#!/bin/bash 

#hosts=('121.40.98.72', '121.40.151.4')
function print_usage(){
  echo "Usage: $0 <121.40.98.72|121.40.151.4>"
}

if [[ -z $1 ]];then
  print_usage;
  exit;
fi
host=$1

mvn clean package;
scp target/lreceiver-jar-with-dependencies.jar liuo@$host:~/lreceiver;
ssh liuo@$host <<EOF
  cd lreceiver;
  ./shutdown.sh;
  ./startup.sh;
EOF
echo "$host receiver redeployed."

