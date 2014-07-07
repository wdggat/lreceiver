#!/bin/sh
kill `ps aux |grep "lreceiver-jar-with-dependencies.jar" |awk '{print $2}'`
