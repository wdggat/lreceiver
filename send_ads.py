#!/usr/bin/python 

import os
import sys

N=10
POS_FILE = 'send_ads.pos'

def get_pos(posf=POS_FILE):
    if not os.path.exists(posf):
        return 0
    line = open(posf).readlines()[0]
    return int(line.strip())

def update_pos(lineno):
    out = open(POS_FILE, 'w')
    out.write('%s' %  lineno)
    out.close()

def get_qqnum_fromline(line):
    if line is None or line.strip() == '':
        return ''
    return line.strip()

def send_ad(qqnums):
    for qq in qqnums:
        print 'Ad sent: %s' % qq

def main(qqpath):
    c, qqnums = 0,set()
    pos_lineno = get_pos()
    for line in open(qqpath):
        c += 1
        if c <= pos_lineno:
            continue
        pos_lineno += 1
        qqnums.add(get_qqnum_fromline(line))
	if len(qqnums) >= N:
	    break
    update_pos(pos_lineno)
    send_ad(qqnums)
        
if __name__ == '__main__':
    main(sys.argv[1])

