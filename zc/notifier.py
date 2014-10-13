#!/usr/bin/env python 

import utils

def notify(content):
    utils.execute("mail -s \"%s\" hzliuxiaolong@163.com" % str(content))

