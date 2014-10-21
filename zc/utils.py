#!/usr/bin/env python 

from datetime import datetime
import os
import time
import subprocess

def make_datetime(sec):
    if len(str(sec)) == 13: 
        sec = int(sec) / 1000
    t = time.localtime(sec)
    return datetime(year = t.tm_year, month = t.tm_mon, day = t.tm_mday, hour = t.tm_hour, minute = t.tm_min, second = t.tm_sec)

def writelines(lines, path, mode = 'w'):
    out = open(path, mode)
    for line in lines:
        out.write(str(line).strip() + os.linesep)
    out.close()
		    
def execute(command):
    print 'Execute --> %s' % command
    return subprocess.check_output(command, stderr=subprocess.STDOUT, shell=True)

