#!/usr/bin/env python 

import utils

def notify(content):
    utils.execute("echo \"%s\" | mail hzliuxiaolong@corp.netease.com -s 'zucai_2s1'" % str(content))

